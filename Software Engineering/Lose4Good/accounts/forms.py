'''
Created on Nov 24, 2013

@author: ArulSamuel
'''
from django import forms
from django.contrib.auth.models import User
from accounts.models import WeightLoser

class LoginForm(forms.Form):
        username=forms.CharField(widget=forms.TextInput(attrs={'placeholder':'Username','size':'20'}))
        password=forms.CharField(widget=forms.PasswordInput(attrs={'placeholder':'Password','size':'20'},render_value=False))
        

class RegistrationForm(forms.Form): #Extend ModelForm
        fname        = forms.CharField(widget=forms.TextInput(attrs={'placeholder': 'First Name'}))
        lname           = forms.CharField(widget=forms.TextInput(attrs={'placeholder': 'Last Name','size':'22'}))
        emailId           = forms.EmailField(widget=forms.TextInput(attrs={'placeholder': 'Email','size':'50'}))
        password        = forms.CharField(widget=forms.PasswordInput(render_value=False,attrs={'placeholder': 'Password','size':'50'}))
        passworda       = forms.CharField(label=(u'Re-Type Password'), widget=forms.PasswordInput(render_value=False,attrs={'placeholder': 'Re-Type Password','size':'50'}))
        CHOICES = (('0', 'Male',), ('1', 'Female'),)
        gender          = forms.ChoiceField(widget=forms.RadioSelect, choices=CHOICES)
        
        

        class Meta:
                model = WeightLoser #power of ModelForm here.
                exclude = ('user',)   #Exclude the creation of these field (above4)

        def clean(self):
                    
                emailId = self.cleaned_data.get('emailId')
                try:
                       
                        
                        User.objects.get(email=emailId)
                        raise forms.ValidationError("This email Id is already registered with the system.")
                except User.DoesNotExist:
                        pass #return emailId
                
                p1="" 
                p2=""
                p1=self.cleaned_data.get('password')
                p2=self.cleaned_data.get('passworda')
             
                if p1 != p2:
                    raise forms.ValidationError("The passwords did not match.  Please try again.")
                
                return self.cleaned_data
            
class ResetForm(forms.Form):
        email=forms.CharField(widget=forms.TextInput(attrs={'placeholder':'Email','size':'50'}))
        