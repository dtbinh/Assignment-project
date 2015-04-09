'''
Created on Nov 22, 2013

@author: ArulSamuel
'''
from django.contrib import admin
from accounts.models import WeightLoser,Charity,FinancialOfficer,Profile


class WeightLoserAdmin(admin.ModelAdmin):
    list_display = ('name', 'emailId','gender')
    
admin.site.register(WeightLoser, WeightLoserAdmin)

class ProfileAdmin(admin.ModelAdmin):
    list_display = ('gender','fid','token')
admin.site.register(Profile, ProfileAdmin)

class CharityAdmin(admin.ModelAdmin):
    list_display = ('charityId','name', 'emailId')
    
admin.site.register(Charity, CharityAdmin)

class FOAdmin(admin.ModelAdmin):
    list_display = ('name', 'emailId')
    
admin.site.register(FinancialOfficer, FOAdmin)