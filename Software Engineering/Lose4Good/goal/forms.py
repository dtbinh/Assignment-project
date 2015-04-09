'''
Created on Nov 27, 2013

@author: ArulSamuel
'''
from django import forms
from goal.models import Goal,  TrackGoal
from accounts.models import Charity
from accounts.models import WeightLoser
from django.contrib.auth.models import User
from functools import partial   # To add DatePicker in the Deadline field
DateInput = partial(forms.DateInput, {'class': 'datepicker'})

class GoalForm(forms.Form):
        
        currWeight=forms.FloatField(widget=forms.TextInput(attrs={'placeholder':'In Pounds'}))
        losepounds=forms.IntegerField(widget=forms.TextInput(attrs={'placeholder':'Max 25lbs','size':'20'}))
        deadline=forms.DateField(widget=forms.TextInput(attrs={'id':'datepicker','placeholder':'deadline','size':'20','readonly':'readonly'}))
    
        Charity_choices=()
        charityobjectlist = Charity.objects.all()
        charitylist=[]
        for charity in charityobjectlist:
                Charity_choices=Charity_choices+((charity.charityId,charity.name),)
        charitylist=forms.ChoiceField(choices=Charity_choices)
                
        class Meta:
            model = Goal
        def clean(self):
                g=self.cleaned_data.get('losepounds')
                cw=self.cleaned_data.get('currWeight')
                if g>25 or g<1:
                    raise forms.ValidationError("The goal should be between 1 and 25 lbs") 
                if cw<=g+1:
                    raise forms.ValidationError("You can't lose more than your current weight!") 
                return self.cleaned_data

                
                
class InviteForm(forms.Form):
    name=forms.CharField(widget=forms.TextInput(attrs={'placeholder':'FullName'}))
    email=forms.EmailField(widget=forms.TextInput(attrs={'placeholder':'Email Id'}))
    message=forms.CharField(widget=forms.Textarea(attrs={'placeholder':'Your Text message','cols': 30, 'rows': 5}))
    
class PaymentForm(forms.Form):
    name=forms.CharField(widget=forms.TextInput(attrs={'placeholder':'Full Name'}))
    email=forms.CharField(widget=forms.TextInput(attrs={'placeholder':'Email Address'}))
    amtperlb=forms.CharField(widget=forms.TextInput(attrs={'placeholder':'Dollars per lb'}))
    payToCharity=forms.BooleanField(widget=forms.CheckboxInput(),required=False)

class PayPalForm(forms.Form):
    amount=forms.CharField(widget=forms.TextInput(attrs={'placeholder':'Dollars per lb'}))

class addWeightForm(forms.Form):
    newWeight=forms.FloatField(widget=forms.TextInput(attrs={'placeholder':'weight'}))

    def __init__(self, user, *args, **kwargs):
        super(addWeightForm, self).__init__(*args, **kwargs)
        try:
            owner=WeightLoser.objects.get(user=user)
            Goal.objects.get(weightloser=owner,status='inprogress')
        except Goal.DoesNotExist:
            raise forms.ValidationError("You Don't have an open Goal! Create a new goal before you can update") 
        
class trackGoalForm(forms.Form):
    
    def __init__(self, user, *args, **kwargs):
        super(trackGoalForm, self).__init__(*args, **kwargs)
        try:
            owner=WeightLoser.objects.get(user=user)
            goals=Goal.objects.get(weightloser=owner,status='inprogress')
            newWeight=forms.FloatField(widget=forms.TextInput(attrs={'placeholder':'weight'}))

            #change to filter to get all the goals
            #track=TrackGoal.objects.filter(goal=goals.goalId)
            #print 'here',track
        except Goal.DoesNotExist:
            raise forms.ValidationError("You Don't have an open Goal! Create a  goal before you can track it") 
               
