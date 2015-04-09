from django.db import models
from accounts.models import Charity,WeightLoser

# Create your models here.

class Goal(models.Model):
    weightloser=models.ForeignKey(WeightLoser)
    goalId=models.AutoField(primary_key=True)
    current_weight=models.FloatField()
    benchmark=models.FloatField()
    target_weight=models.FloatField()
    creation_date=models.DateField(auto_now=True)
    deadline=models.DateField()
    charity=models.ForeignKey(Charity)
    status=models.CharField(max_length=10)  #Setting default values to the status value
    toLose=models.FloatField(max_length=10) # how much he/she wants to loses\
    progress=models.FloatField(max_length=10)
    def __unicode__(self):
        return u'%s'%(self.goalId)
    

    
class TrackGoal(models.Model):
    weight=models.FloatField()
    goal=models.ForeignKey(Goal)
    track_date=models.DateField(auto_now=True)
    
    def __unicode__(self):
        return u'%s on %s'%(self.track_date,str(self.weight))

class Sponsor(models.Model):
    goal=models.ManyToManyField(Goal)
    sponsorId=models.AutoField(primary_key=True)
    name=models.CharField(max_length=40)
    emailId=models.EmailField()
    
    def __unicode__(self):
        return u'%s'%(self.sponsorId)    
