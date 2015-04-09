from django.contrib.auth import authenticate, login, logout
from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User
from django.http import HttpResponseRedirect
from django.shortcuts import render_to_response
from django.template import RequestContext
from accounts.forms import RegistrationForm, LoginForm, ResetForm
from accounts.models import WeightLoser
from django.contrib.auth.views import password_reset, password_reset_confirm
from django.core.urlresolvers import reverse
from django.shortcuts import render
from goal.views import setCheckActiveGoal,checkActiveGoal
from django.contrib import messages


'''
Description:    This method allows a new user to register with the lose4good
                system and also logs in the user. Then navigates the user to 
                his profile page.
'''
def WeightloserRegistration(request):
    if request.user.is_authenticated():
        return HttpResponseRedirect('/profile/') 
    
    if request.method=='POST':  
        form=RegistrationForm(request.POST)
        if form.is_valid():  
            emailtemp=str(form.cleaned_data['emailId'])
            name=emailtemp
            user = User.objects.create_user(username=name, email = form.cleaned_data['emailId'], password = form.cleaned_data['password'])
            user.save()
            fname=form.cleaned_data['fname']
            lname=form.cleaned_data['lname']
            fullname=fname+' '+lname
            weightloser = WeightLoser(user=user, name=fullname,gender=form.cleaned_data['gender'],emailId=form.cleaned_data['emailId'])
            weightloser.save()
            userwloser = authenticate(username=name, password=form.cleaned_data['password'])
            login(request, userwloser)
            return HttpResponseRedirect('/profile/')
        else:  #display the form when not valid
            return render_to_response('RegisterPage.html',{'form':form}, context_instance=RequestContext(request))
    else:    #Showing the form
        '''user is not submitting the form, show them a blank registration form'''
        form=RegistrationForm()
        context={'form':form}
        return render_to_response('RegisterPage.html',context, context_instance=RequestContext(request))

'''
Description:    This method provides displays the profile page for the user 
                and allows the user to view his goal or create a new goal if
                a goal hasn't been created.
'''
   

def checkFinacial(request):
       if request.user.username != 'financialofficer@l4g.com':
            messages.error(request, 'You are unauthorized to access this section of the website!', 'unauthorized')
            return HttpResponseRedirect('/login/') 
        
@login_required
def Profile(request):
        if request.user.username == 'financialofficer@l4g.com':
            return HttpResponseRedirect('/finance/') 
        if not request.user.is_authenticated():
            return HttpResponseRedirect('/login/')
        weightloser = request.user.get_profile
        flag=False
        if checkActiveGoal(request):
            flag=True
        context = {'weightloser': weightloser,'flag':flag}
        return render_to_response('HomePage.html', context, context_instance=RequestContext(request))
        
'''
Description:    This method allows the user to login to the user and validating
                his credentials
'''    
def LoginRequest(request):
        if request.user.is_authenticated():
                return HttpResponseRedirect('/profile/')
        if request.method == 'POST':
                form = LoginForm(request.POST)
                if form.is_valid():
                        username = form.cleaned_data['username']
                        password = form.cleaned_data['password']
                        weightloser = authenticate(username=username, password=password)
                        if weightloser is not None: 
                                login(request, weightloser)
                                setCheckActiveGoal(request,'C') # from goal.views to check if user has active goal
                                return HttpResponseRedirect('/profile/')
                        else:
                                return render_to_response('LoginPage.html', {'form': form}, context_instance=RequestContext(request))
                else:
                        return render_to_response('LoginPage.html', {'form': form}, context_instance=RequestContext(request))
        else:
                ''' user is not submitting the form, show the login form '''
                form = LoginForm()
                context = {'form': form}
                return render_to_response('LoginPage.html', context, context_instance=RequestContext(request))
'''
Description:    This method logs the user out of the system and terminates his 
                session.
'''
@login_required
def LogoutRequest(request):
        logout(request)
	context={}
        return render_to_response('LogoutPage.html',context, context_instance=RequestContext(request))

def OpenPage(request):

        context={}

        if request.user.is_authenticated():

            return HttpResponseRedirect('/profile/')  

        else:

            return render_to_response('OpenPage.html',context, context_instance=RequestContext(request))
        
def AboutUs(request):
        context={}
        return render_to_response('About.html',context, context_instance=RequestContext(request))


'''
Description:    This method displays the profile page to the user.
'''       
@login_required
def DisplayProfile(request):
        context={}
        return render_to_response('HomePage.html',context,'')
'''
Description:    This method allows the user to request for password reseting.
'''
def ResetRequest(request):
        if request.method == 'POST':
                form = ResetForm(request.POST)
                if form.is_valid():
                        return password_reset(request, template_name='password_reset_form.html',
                                              email_template_name='password_reset_email.html',
                                              subject_template_name='password_reset_email_subject.html',
                                              post_reset_redirect=reverse('success'))
                        
                else:
                        return render_to_response('password_reset_form.html', {'form': form}, context_instance=RequestContext(request))
        else:
                ''' user is not submitting the form, show the login form '''
                form = ResetForm()
                context = {'form': form}
                return render_to_response('password_reset_form.html', context, context_instance=RequestContext(request))
'''
Description:    This method provides the user a chance to enter the new password.
'''
def ResetConfirm(request, uidb36=None, token=None):
    # Wrap the built-in reset confirmation view and pass to it all the captured parameters like uidb64, token
    # and template name, url to redirect after password reset is confirmed.
    return password_reset_confirm(request, template_name='password_reset_confirm.html',
        uidb36=uidb36, token=token, post_reset_redirect=reverse('complete'))   
'''
Description:    This method redirects the user to the reset email sent page.
'''    
def Success(request):
    return render(request, 'password_reset_done.html')         
'''
Description:    This method redirects the user to the reset success page.
'''
def Complete(request):
    return render(request, 'password_reset_complete.html')



def Test(request):
    return render(request,'test123.html')


@login_required
def Finance(request):
    if request.user.username != 'financialofficer@l4g.com':
            messages.error(request, 'Sorry..You are unauthorized  to access this section of the website!', 'unauthorized')
            return HttpResponseRedirect('/login/') 
    context={}
    return render_to_response('FinancialOfficer.html',context,context_instance=RequestContext(request))
