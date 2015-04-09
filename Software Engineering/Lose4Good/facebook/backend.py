import json
import urlparse
import urllib
import datetime
 


from django.conf import settings
from django.contrib.auth.models import User
from django.core.urlresolvers import reverse
from django.db import IntegrityError
from django.http import HttpResponseRedirect
from accounts.models import WeightLoser

from accounts.models import Profile

class FacebookBackend:
  def authenticate(self, token=None, request=None):
    """
    Facebook authentication

    Retrieve access token and Facebook data. Determine if user already has a
    profile. If not, a new profile is created using either the user's
    username or Facebook id. Finally, the user's Facebook data is saved.
    """
    args = {
      'client_id': settings.FACEBOOK_APP_ID,
      'client_secret': settings.FACEBOOK_APP_SECRET,
      'redirect_uri': request.build_absolute_uri(reverse('facebook_callback')),
      'code': token,
    }

    # Retrieve access token
    url = urllib.urlopen('https://graph.facebook.com/oauth/access_token?%s' %
                          urllib.urlencode(args)).read()
    response = urlparse.parse_qs(url)
    access_token = response['access_token'][-1]
    # Retrieve user's public profile information
    data = urllib.urlopen('https://graph.facebook.com/me?access_token=%s' %
                           access_token)
    fb = json.load(data)

    try:
		# Check if user profile exists
		profile = Profile.objects.get(fid=fb['id'])
		user = profile.user

		# Update access token
		profile.token = access_token
		profile.save()
		return user 
		#return HttpResponseRedirect('/profile/')
    except Profile.DoesNotExist:
		# User profile doesn't exist. Create new user.
		username = fb.get('username', fb['id'])
		# Create 7th Feb 2011
		d = datetime.date(2011,2,7)
		user = User.objects.create_user(username=username, email=fb['email'])
		user.first_name = fb['first_name']
		user.last_name = fb['last_name']
		name1=user.first_name+' '+user.last_name
		gen=fb['gender']
		if gen.lower()=='male': x=True
		else: x=False
		user.save()
		weightloser = WeightLoser(user=user,name=name1 ,gender=x,emailId=fb['email'])    
		weightloser.save()
		# Create and save core user
		profile = Profile.objects.create(user=user, token=access_token,
                                       fid=fb['id'], gender=fb['gender'])
		profile.save()

		return user

  def get_user(self, user_id):
    """ Returns user of a given ID. """
    try:
      return User.objects.get(pk=user_id)
    except User.DoesNotExist:
      return None

    supports_object_permissions = False
    supports_anonymous_user = True
