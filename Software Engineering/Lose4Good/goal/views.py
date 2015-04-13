from django.contrib.auth.decorators import login_required
from django.shortcuts import render_to_response
from django.template import RequestContext
from django.http import HttpResponseRedirect
from django.core.mail import send_mail
from accounts.models import Charity, WeightLoser
from goal.forms import GoalForm, InviteForm, PaymentForm, addWeightForm, PayPalForm, trackGoalForm
from goal.models import Goal, TrackGoal, Sponsor
from django.contrib import messages
from payment.models import Transaction,ApproveTransaction
from django.shortcuts import render
import datetime
import base64
from datetime import date
from django.contrib.sites.models import Site

#Handle if goal has already been created, then HTTTPRedirect to TrackGoal
'''
Description:    This method creates the goal and updates the Goal table
                and adds the first entry into the trackGoal for that 
                particular goal.
'''

'''
Author: Ali Alotaibi
Description:    This method is used to set the session variable that check if the user has active goal or not
                  that way we don't need to check the database everytime we want to know that.. for example when 
                  we create a goal the method will be called with parameter (request, T) T means set that the user has 
                  active goal. If the goal is achieved or deleted it will be called with F parameter 
                  so the session will be set to False and the user will be able to create a new one, 
                and adds the first entry into the trackGoal for that. C is used if not sure(used after login)
'''
def setCheckActiveGoal(request,kind):
    if kind=='T':
        request.session['has_Active_Goal']=True  # need to add logic to check if the goal is active or completed
    elif kind=='F':
        request.session['has_Active_Goal']=False
    elif kind=='C':
        try:
            owner=WeightLoser.objects.get(user=request.user)
            Goal.objects.get(weightloser=owner,status='inprogress') # need to add logic to check if the goal is active or completed
            request.session['has_Active_Goal']=True
        except Goal.DoesNotExist:
            request.session['has_Active_Goal']=False
        
    
'''Author:Ali ALotaibi
    Test if the user has active goal'''        
def checkActiveGoal(request):
    if request.session.get('has_Active_Goal', False)==True:
        return True
    else:
        return False
 
    
@login_required
def CreateGoal(request):
    storage = messages.get_messages(request)
    storage.used = True
    if  not checkActiveGoal(request):
        if request.method=='POST':  
            form=GoalForm(request.POST)
            if form.is_valid():  
                goal = Goal()
                goal.deadline=form.cleaned_data.get('deadline')
                weightloser=WeightLoser.objects.get(user=request.user)
                goal.current_weight=float(str(form.cleaned_data['currWeight']))
                goal.target_weight=float(str(form.cleaned_data['currWeight'])) - float(str(form.cleaned_data['losepounds']))
                charity=Charity.objects.get(charityId=form.cleaned_data['charitylist'])
                goal = Goal(weightloser=weightloser,deadline=form.cleaned_data['deadline'],status='inprogress')
                goal.charity=charity
                goal.current_weight=float(str(form.cleaned_data['currWeight']))
                goal.target_weight=float(str(form.cleaned_data['currWeight'])) - float(str(form.cleaned_data['losepounds']))
                goal.benchmark=goal.current_weight
                goal.toLose=(goal.current_weight-goal.target_weight)
                goal.progress=0
                goal.save()
               # goal=Goal.objects.get(weightloser=weightloser) #check Ali 
                trackGoal=TrackGoal()
                trackGoal.goal=goal
                trackGoal.weight=goal.current_weight
                trackGoal.track_date=goal.creation_date
                trackGoal.save()
                setCheckActiveGoal(request,"T") # set that the user has active goal
                messages.success(request, 'the goal has been created, now it is time to get some sponsors!', 'Created_created')
                return HttpResponseRedirect('/inviteSponsor/')
            else:  #display the form when not valid
                return render_to_response('CreateGoal.html',{'form':form}, context_instance=RequestContext(request))
        else:    #Showing the form
            '''blank Form'''
            weightloser=WeightLoser.objects.get(user=request.user)
        
            form=GoalForm()
            context={'form':form}
            return render_to_response('CreateGoal.html',context, context_instance=RequestContext(request))
    else:
        messages.error(request, "You already have an active Goal, you can't have more than 1 active goal at a time", "Goal_Created")
        return HttpResponseRedirect('/addWeight/')
        
        
'''
Description:    This method navigates the user to the Goal created page
                once it has been created.+
'''    
def GoalCreated(request):
    return render_to_response('GoalCreated.html','','')

'''
Author:Omkar Yerunkar
Description:    This method is responsible for inviting the sponsor and sends 
                an email with the desired message.
'''
@login_required
def InviteSponsor(request):
    if request.method=='POST':  
        form=InviteForm(request.POST)
        if form.is_valid():       
            weightLoser=WeightLoser.objects.get(user=request.user)
            goalId=getCurrentGoal(request)
            goal=Goal.objects.get(goalId=goalId)
            loseWeight = goal.current_weight - goal.target_weight          
            message=form.cleaned_data['message']
            sponsor_email=form.cleaned_data['email']
            recepients =[]
            recepients.append(sponsor_email)
            emailBody = 'Dear ' + form.cleaned_data['name'] +',\nYou have been invited to sponsor ' + weightLoser.name + ' in order to lose ' + str(loseWeight) + ' lb. If you are willing to do so, please click:\nhttp://' + Site.objects.get_current().domain + '/payment/' + base64.urlsafe_b64encode(str(goalId)) + '/\n\n' + message
            send_mail('Lose4Good Sponsor', emailBody,'LOSE4GOOD.org<arulrs2k9@gmail.com>', recepients)
            return HttpResponseRedirect('/inviteConfirm/')
        else: 
            return render_to_response('InviteSponsor.html',{'form':form}, context_instance=RequestContext(request))
        
    else:    #Showing the form
        '''blank Form'''
        goalId=getCurrentGoal(request)
        form=InviteForm()
        context={'form':form,'goalId':goalId}
        return render_to_response('InviteSponsor.html',context, context_instance=RequestContext(request))

'''
Author:Omkar Yerunkar
Description:    This method send the confirmation for inviting sponsors

'''

def InviteConfirmation(request):
	print "here"
	context={}
	return render_to_response('InviteConfirm.html',context,context_instance=RequestContext(request))

def InviteFB(request):
    goalId=getCurrentGoal(request)
    context={'goalId':base64.urlsafe_b64encode(str(goalId)),'domain':Site.objects.get_current().domain}
    return render_to_response('InviteFB.html',context,context_instance=RequestContext(request))

def PostFB(request):
    context={'domain':Site.objects.get_current().domain}
    return render_to_response('AchieveFB.html',context,context_instance=RequestContext(request))
    
'''
Author:Omkar Yerunkar
Description:    This method navigates the user to the payment page on receiving the 
                invitation link in his email account.
''' 

def PaymentPage(request,goalId=None):
    if request.method=='POST':  
        form=PaymentForm(request.POST)
        if form.is_valid():
            goal=Goal.objects.get(goalId=base64.urlsafe_b64decode(str(goalId)))
            
            sponsor=Sponsor()
            sponsor.name=form.cleaned_data['name']
            sponsor.emailId=form.cleaned_data['email']        
            sponsor.save()
            
            transaction=Transaction()
            transaction.sponsor=sponsor
            transaction.goal=goal
            transaction.amount_per_lb = form.cleaned_data['amtperlb']
            transaction.require_refund = not(form.cleaned_data['payToCharity'])
            #transaction.status = True
            transaction.save()
            
            return HttpResponseRedirect('/paypal/' + base64.urlsafe_b64encode(str(transaction.transaction_id)))
        else:  #display the form when not valid
            return render_to_response('PaymentPage.html',{'form':form}, context_instance=RequestContext(request))
    else:   
        '''blank Form'''
        ''' Check if the goal still active'''
        try:
            Goal.objects.get(goalId=base64.urlsafe_b64decode(str(goalId)),status='inprogress')
        except Goal.DoesNotExist:
            context={'is_Active':True}
            return render_to_response('PaymentPage.html',context, context_instance=RequestContext(request))
        form=PaymentForm()
        goal=Goal.objects.get(goalId=base64.urlsafe_b64decode(str(goalId)))
        name=goal.weightloser.name
        loseWeight=goal.current_weight-goal.target_weight
        period=goal.deadline-goal.creation_date
        period=period.days
        charity=goal.charity.name

        context={'form':form,'name':name,'loseWeight':loseWeight,'period':period,'charity':charity}
        return render_to_response('PaymentPage.html',context, context_instance=RequestContext(request))

'''
Author:Omkar Yerunkar
Description:    This method navigates the user to the donation confirmation page 
                where the user could be redirected to PayPal.
''' 
def PayPalDonate(request,transactionId=None):
    if request.method=='POST':  
        form=PayPalForm(request.POST)
        if form.is_valid():
            return HttpResponseRedirect('')
        else:  #display the form when not valid
            return render_to_response('PayPalDonate.html',{'form':form}, context_instance=RequestContext(request))
    else:   
        '''blank Form'''
        form=PaymentForm()
        transaction=Transaction.objects.get(transaction_id=base64.urlsafe_b64decode(str(transactionId)))
        goal=Goal.objects.get(goalId=transaction.goal.goalId)
        
        #transaction.status = True
        #transaction.save()
                
        amount = transaction.amount_per_lb*(goal.current_weight-goal.target_weight)
        context={'form':form, 'transactionId':base64.urlsafe_b64decode(str(transactionId)), 'amount':amount, 'domain':Site.objects.get_current().domain}
        return render_to_response('PayPalDonate.html',context, context_instance=RequestContext(request))

'''
Author:Ali ALotaibi
Description:    This method provides the user with a confirmation that the 
                goal has been updated.
'''
@login_required 
def weightAdded(request):

        weightloser = request.user.get_profile
        context = {'weightloser': weightloser}
        return render_to_response('weightUpdated.html', context, context_instance=RequestContext(request))
   
'''
Author: Ali Alotaibi 
Description:  Check if Goal is achieed or not
'''
@login_required
def isAchieved(request,newweight,goal):
    if goal.status=="completed":
        messages.error(request, "The Goal has already been achieved. You need to create a new goal before you update your weight", 'Goal_Done')
        return -1 # Already been achieved
    elif goal.status=="expired":
        messages.error(request, "The Goal has already expired. You need to create a new goal before you update your weight", 'Goal_Done')
        return -1 # Already expired
    # we need here to check if the deadline not reached 
    elif newweight<=goal.target_weight:
        messages.success(request, 'Congratulation, You have achieved your Goal!', 'Done_Done')
        #messages.success(request,'Click <a href="/AchieveFB/">Here</a> to share on Facebook','Share_FB')
        goal.status="completed"
        setCheckActiveGoal(request,"F")
        
        return 1 # Achieved
    else:
               return 0 # Not achieved yet
         
'''
Author: Ali Alotaibi, Arul Samuel 
Description:    This method allows the user to update weight for his goal.
		It allows calculates the transaction to Charity
'''        

@login_required    
def addWeight(request): 
    storage = messages.get_messages(request) # cleans any remaining messages
    storage.used = True
    if checkActiveGoal(request):
        flag = True
        if request.method=='POST':
            form=addWeightForm(request.user,request.POST)
            weightloser = request.user.get_profile
            if form.is_valid():
                owner=WeightLoser.objects.get(user=request.user)
                goal1=Goal.objects.get(weightloser=owner,status='inprogress')
                weighted=form.cleaned_data['newWeight']
                current=goal1.current_weight
                result=isAchieved(request,weighted,goal1)
                if result!=-1: # goal was not achieved in the past so we can update the weight
                    trackgoal=TrackGoal(weight=weighted,goal=goal1)
                    goal1.current_weight=weighted
                    if(goal1.current_weight < goal1.benchmark):
        			#targetweight into account
        			if(weighted < goal1.target_weight):
        				lostWeight=goal1.benchmark-goal1.target_weight
        			else:			
        				lostWeight=goal1.benchmark-weighted
        		    	goal1.benchmark=weighted
        			#get each sponsor for the goal, add into approveTransaction
        			payments=Transaction.objects.filter(goal=goal1)
        			totalMoneyPaid=0.0
        			for payment in payments:
        				totalMoneyPaid+= (payment.amount_per_lb * lostWeight)
                    if(totalMoneyPaid > 0.0):
                        addApprovalRecord(goal1,totalMoneyPaid,goal1.charity.name,goal1.charity.emailId)
                    progress=getProgress(request,goal1)
                    goal1.progress=progress
                    goal1.save()
                    trackgoal.save()
            if result==0:
                if current>weighted:
                     messages.success(request, "Your weight has been updated.Congrats on losing weight, your are on the right track ", 'Goal_updated_less') 
                elif current == weighted:
                    messages.success(request, "Your weight has been updated.Congrats on losing weight, your are on the right track ", 'Goal_updated_still') 
                elif current < weighted:
                    messages.success(request, "Your weight has been updated.Congrats on losing weight, your are on the right track ", 'Goal_updated_more')
                return HttpResponseRedirect('/trackGoal/')
	        if result ==-1 or result == 1:
                    flag = False
                else:
                    flag = True
                context = {'flag':flag}
                return render_to_response('weightUpdated.html',context, context_instance=RequestContext(request))
               
                    
            else:
                return render_to_response('weightUpdated.html',{'form':form}, context_instance=RequestContext(request))
        else:
                #Showing the form
            form=addWeightForm(request.user)
            weightloser = request.user.get_profile
            context={'form':form, 'weightloser': weightloser}
            return render_to_response('weightUpdated.html',context, context_instance=RequestContext(request))
    else:
        messages.error(request, "you have to create a goal first", 'creategoalfirst')
        return HttpResponseRedirect('/createGoal/')
          
'''
Author: Arul Samuel, Omkar Yerunkar
Description:  This method adds a record into the ApproveTransaction table
'''

def addApprovalRecord(goal,amount,payeeName,payeeEmail):
	approveTransaction=ApproveTransaction()
	approveTransaction.goal=goal
	approveTransaction.transactionAmount=amount
	approveTransaction.approvalStatus=False
	approveTransaction.targetPayeeName=payeeName
	approveTransaction.targetPayeeEmailId=payeeEmail
	approveTransaction.save()

'''
Author: Arul Samuel 
Description:  This method adds a record into the ApproveTransaction table
'''
def ReachDeadline():
    goalList=Goal.objects.all()
    dateToday=date.today()
    for goal in goalList:
        if(dateToday>=goal.deadline):
            print 'Goal Terminated here'
            goal.status="expired"
            goal.save()
'''
Author: Omkar Yerunkar 
Description:  This method is reponsible for tracking the goal
'''
@login_required
def GoalTrack(request):
    ReachDeadline()
    setCheckActiveGoal(request,"C")
    if  checkActiveGoal(request):
            form=addWeightForm(request.user)
            owner=WeightLoser.objects.get(user=request.user)
            goals=Goal.objects.get(weightloser=owner,status='inprogress')
            track=TrackGoal.objects.filter(goal=goals.goalId).order_by('-track_date','weight')
            list1=[]
            list2=[]
            count1=0
            count=0
            origWeight=0
            createDate=goals.creation_date
            
            for t1 in track:
                count1+=1
                if len(track)==count1:
                    createDate=t1.track_date
                print(createDate)
            for t in track:
                count+=1      
                period=t.track_date-createDate
                list1.append(period.days)
                list2.append(t.weight)
                if len(track)==count:
                    origWeight=t.weight
               
                    
            currWeight=goals.current_weight
            targetWeight=goals.target_weight
            print("Current Weight: ",currWeight)
            print("Target Weight: ",targetWeight)
            
            remaining=goals.current_weight-goals.target_weight
            completed = origWeight - goals.current_weight
            progress=goals.progress
            
            
            if completed <0:
                completed=0
                
            completedPercentage=(completed/(origWeight-targetWeight))*100
            remainingPercentage=100-completedPercentage
            
            print("Remaining: ",remaining)
            print("Completed: ",completed)
            
            print("Remaining%: ",remainingPercentage)
            print("Completed%: ",completedPercentage)
            
            
       
       
        #list1=[5,10,15,20]
        #list2=[100,90,80,70]
            
       
            context={'form':form,'array1':list1,'array2':list2,'remaining':remaining,'completed':completed,'cPercent':completedPercentage,\
                     'rPercent':remainingPercentage,'targetWeight':targetWeight,'currentWeight':currWeight,'deadline':goals.deadline,\
                     'charity':goals.charity,'progress':progress}
            return render_to_response('TrackGoal.html',context,context_instance=RequestContext(request))
    else:
	    messages.error(request, "Create a new goal before you can track your progress", "Goal_To_be_created")
	    return HttpResponseRedirect('/createGoal/')
        

@login_required 
def DeleteGoal(request):
    if  checkActiveGoal(request):
            owner=WeightLoser.objects.get(user=request.user)
            goals=Goal.objects.get(weightloser=owner,status='inprogress')
            sponsor=Sponsor.objects.filter(goal=goals.goalId)
            deletestatus=True
            if len(sponsor)>0:
                deletestatus=False
            
                
            else:
                deletestatus=True
                goals.delete()
                setCheckActiveGoal(request,'F')
            
                
            context={'status':deletestatus,'delete_cliked':True}
          
            return render_to_response('HomePage.html',context,context_instance=RequestContext(request))
     
    else:
     messages.error(request, "You already have an active Goal, you can't have more than 1 active goal at a time", "Goal_Created")
     #Check the navigation	
     return HttpResponseRedirect('/createGoal/')

'''
Author: Ali Alotaibi, Omkar Yerunkar 
Description:  This method retrieve active Goal ID
'''
def getCurrentGoal(request):
    weightLoser=WeightLoser.objects.get(user=request.user)
    goalid=0
    if checkActiveGoal(request):
        goal=Goal.objects.get(weightloser=weightLoser,status='inprogress')
        goalid=goal.goalId
    return goalid

def getProgress(request,goal):
        res=0.0
        g1=goal
        res=0.0
        res= (g1.current_weight-g1.target_weight)
        if res<0:
             res=0
        res=1-(res/g1.toLose)
        res=res*100
        return res