import json
import urllib

from django import template
from django.contrib.auth.models import User

from accounts.models import Profile
from facebook.utils import fb, valid_token as token

register = template.Library()

@register.simple_tag
def facebook_graph(self):
  """ Facebook graph for a specific user. """
  data = urllib.urlopen('%s' % fb('me', self, token=True))
  results = json.load(data)
  return results

@register.simple_tag
def facebook_picture(self, size):
  """
  Facebook profile picture

  Retrieve a user's Facebook profile image. Sizes include "square" (50x50),
  "small" (50 pixels wide, variable height), "normal" (100 pixels wide,
  variable height) and "large" (200 pixels wide, variable height). 
  """
  results = '%s?type=%s' % (fb('%s/picture' % self), size)
  return results

@register.filter
def valid_token(user):
  """ Check a user's Facebook token is still valid. """
  results = None
  if user.is_authenticated():
    if token(user):
      results = True

  return results
