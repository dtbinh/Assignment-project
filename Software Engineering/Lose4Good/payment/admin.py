'''
Created on Nov 9, 2013

@author: ArulSamuel
'''
from django.contrib import admin

from payment.models import Transaction,ApproveTransaction

class TransactionAdmin(admin.ModelAdmin):
    list_display = ('transaction_id','transaction_date','amount_per_lb','require_refund')
    #fields = ('transaction_id','transaction_date','amount_per_lb','require_refund')
    
admin.site.register(Transaction, TransactionAdmin)

class ApproveTransactionAdmin(admin.ModelAdmin):
	list_display=('goal','transactionAmount','approvalStatus','transaction_date')
	#fields = ('goal','transactionAmount','approvalStatus')

admin.site.register(ApproveTransaction, ApproveTransactionAdmin)
