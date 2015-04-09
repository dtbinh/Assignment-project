from django.db import models
from payment.models import Transaction
# Create your models here.

class paymentDetails(models.Model):
    
    money_rem=models.FloatField()
    money_update_date=models.DateField()
    transaction_id=models.ForeignKey(Transaction)
    
    