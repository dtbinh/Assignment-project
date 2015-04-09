from django.db import models
from datetime import datetime
from goal.models import Goal,Sponsor

# Create your models here.

class Transaction(models.Model):
    sponsor=models.ForeignKey(Sponsor)
    goal=models.ForeignKey(Goal)
    #transaction_id=models.CharField(max_length=15)
    transaction_id=models.AutoField(primary_key=True)
    #transaction_date=models.DateField(datetime.now())
    transaction_date=models.DateField(default=datetime.now())
    amount_per_lb=models.FloatField()
    require_refund=models.BooleanField()
    status=models.BooleanField()

class ApproveTransaction(models.Model):
	approveId=models.AutoField(primary_key=True)
	goal=models.ForeignKey(Goal)
	transactionAmount=models.FloatField()
	approvalStatus=models.BooleanField()
    	targetPayeeName=models.CharField(max_length=40)
    	targetPayeeEmailId=models.EmailField()
        transaction_date=models.DateTimeField(default=datetime.now())