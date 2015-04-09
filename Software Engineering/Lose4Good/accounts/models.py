from django.db import models
from django.contrib.auth.models import User

# Create your models here.
class Profile(models.Model):
  """ User profile model. """
  user = models.OneToOneField(User)
  gender = models.CharField(max_length=6, blank=True)
  fid = models.BigIntegerField(verbose_name=u'Facebook ID')
  token = models.CharField(max_length=150)

  def __unicode__(self):
    return u'%s' % (self.user)    
class WeightLoser(models.Model):
    user = models.OneToOneField(User) 
    name=models.CharField(max_length=60)
    password = models.CharField(max_length=20)
    gender = models.BooleanField(blank=True)
    emailId=models.EmailField()
        
    def __unicode__(self):
        return u'%s'%(self.name)
    
class FinancialOfficer(models.Model):
    user=models.OneToOneField(User)
    name=models.CharField(max_length=60)
    password = models.CharField(max_length=20)
    emailId=models.EmailField()
    #Another field for maintaining type of user?
    
class Charity(models.Model):
    name=models.CharField(max_length=100)
    emailId=models.EmailField()
    charityId=models.AutoField(primary_key=True)
    
    def __unicode__(self):
        return u'%s'%(self.name)
    
    
    
