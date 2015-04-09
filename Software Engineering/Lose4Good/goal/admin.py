'''
Created on Nov 22, 2013

@author: ArulSamuel
'''

from django.contrib import admin
from goal.models import Goal,TrackGoal,Sponsor

class GoalAdmin(admin.ModelAdmin):
    list_display = ('goalId','weightloser','target_weight', 'creation_date','deadline','charity','status','toLose') #displaying the fields
 #   fields = ('target_weight','deadline','charity','status','current_weight') #fields for entering records
    
admin.site.register(Goal, GoalAdmin)


class TrackGoalAdmin(admin.ModelAdmin):
    list_display = ('weight','track_date','goal')
    
admin.site.register(TrackGoal, TrackGoalAdmin)

class SponsorAdmin(admin.ModelAdmin):
    list_display = ('sponsorId','name', 'emailId')
    
admin.site.register(Sponsor, SponsorAdmin)
