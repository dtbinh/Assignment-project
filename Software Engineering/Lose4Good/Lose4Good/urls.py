from Lose4Good import settings
from django.conf.urls import patterns, include, url
from django.contrib import admin
from django.views.generic.simple import direct_to_template
from django.conf.urls.static import static
from django.contrib.staticfiles.urls import staticfiles_urlpatterns

admin.autodiscover()
urlpatterns = patterns('',
                       
    #Admin and Media
    url(r'^admin/', include(admin.site.urls)),
    (r'^media/(?P<path>.*)$', 'django.views.static.serve',
                 {'document_root': settings.MEDIA_ROOT}),
    (r'^static/(?P<path>.*)$', 'django.contrib.staticfiles.views.serve',
		 {'document_root': settings.STATIC_ROOT}),
    
    #Login Logout Register
    (r'^$', 'accounts.views.OpenPage'),
    (r'^login/$', 'accounts.views.LoginRequest'),
    (r'^logout/$', 'accounts.views.LogoutRequest'),
    (r'^register/$','accounts.views.WeightloserRegistration'),
    (r'^profile/$','accounts.views.Profile'),
    (r'^test/$','accounts.views.Test'),
    (r'^finance/$','accounts.views.Finance'),
    (r'^about/$','accounts.views.AboutUs'),
    
    #Create Goal
    (r'^createGoal/$','goal.views.CreateGoal'),
    (r'^goalCreated/$','goal.views.GoalCreated'),
    (r'^inviteSponsor/$','goal.views.InviteSponsor'),
    (r'^payment/(?P<goalId>.+)/$', 'goal.views.PaymentPage'),
    (r'^paypal/(?P<transactionId>.+)/$','goal.views.PayPalDonate'),
    (r'^inviteConfirm/$','goal.views.InviteConfirmation'),
    (r'^PaymentSuccessful/$','payment.views.PaySuccessful'),
    
        
    #TrackGoal
    (r'^addWeight/$','goal.views.addWeight'),
    (r'^weightAdded/$','goal.views.weightAdded'),
    (r'^trackGoal/$', 'goal.views.GoalTrack'),
    (r'^goalDelete/$', 'goal.views.DeleteGoal'),
    
    # Reset Password
    (r'^reset/(?P<uidb36>[0-9A-Za-z]+)-(?P<token>.+)/$', 'accounts.views.ResetConfirm'),
    (r'^resetpassword/$', 'accounts.views.ResetRequest'),
    url(r'^reset/success/$', 'accounts.views.Success', name='success'),
    url(r'^reset/complete/$', 'accounts.views.Complete', name='complete'),

	#Facebook
    url(r'^facebook/', include('facebook.urls')),
	(r'^invalid/$', direct_to_template, {'template': 'invalid.html'}),
    (r'^SponsorFB/$', 'goal.views.InviteFB'),
    (r'^AchieveFB/$', 'goal.views.PostFB'),

    #report
    url(r'^report/$','payment.views.Progress'),
    url(r'^approval/$','payment.views.Approve'),
    url(r'^send/$','payment.views.SendMoney'),

    #trial
    #url(r'^temp/$','goal.views.ReachDeadline'),
)
