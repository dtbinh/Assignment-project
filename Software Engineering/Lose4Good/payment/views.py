from django.template import RequestContext
from django.shortcuts import render_to_response
from goal.models import Goal
from payment.models import ApproveTransaction
from collections import deque
from accounts.views import checkFinacial
from django.contrib.auth.decorators import login_required

from django.http import HttpResponseRedirect

from django.contrib import messages
'''
Description:   This method is responsible for displaying the goal progress for 
		the weightlosers to the financial officer. It displays the goal id,
		startdate, deadline and charityname for the goal.
'''
@login_required
def Progress(request):
	if request.user.username != 'financialofficer@l4g.com':
            messages.error(request, 'Sorry..You are unauthorized  to access this section of the website!', 'unauthorized')
            return HttpResponseRedirect('/login/') 
	
	goalList = Goal.objects.all()
	context={'list':goalList}
	return render_to_response('Report.html',context, context_instance=RequestContext(request))

@login_required
def Approve(request):
	if request.user.username != 'financialofficer@l4g.com':
            messages.error(request, 'Sorry..You are unauthorized  to access this section of the website!', 'unauthorized')
            return HttpResponseRedirect('/login/') 

	list=[]
	if request.method=='POST':
		list=request.POST.getlist("checkbox")
		for approved in list:
			approvedtransaction=ApproveTransaction.objects.get(approveId=approved)
			approvedtransaction.approvalStatus=True
			approvedtransaction.save()
	transactionList = ApproveTransaction.objects.filter(approvalStatus=False,transactionAmount__gt=0.0)
	print transactionList
	context={'list':transactionList}
	return render_to_response('Approval.html',context,context_instance=RequestContext(request))
@login_required
def PaySuccessful(request):
	checkFinacial(request)
	context={}
	return render_to_response('PaySuccessful.html',context,context_instance=RequestContext(request))
@login_required
def SendMoney(request):
	if request.user.username != 'financialofficer@l4g.com':
            messages.error(request, 'Sorry..You are unauthorized  to access this section of the website!', 'unauthorized')
            return HttpResponseRedirect('/login/') 
	transactionList = ApproveTransaction.objects.all()
	moneyList={}
	for transaction in transactionList:
		goal=transaction.goal
		charityName=goal.charity
		money=0.0;
		if charityName in moneyList.keys():
			money=moneyList[charityName]
		money=money+transaction.transactionAmount
		moneyList[charityName]=money
		print 'comes in',moneyList[charityName]			
	context={'dict':moneyList}
	
	return render_to_response('SendMoney.html',context,context_instance=RequestContext(request))
