from django.conf import settings
from django.http import HttpResponseRedirect
from django.utils.functional import wraps

from facebook.utils import valid_token

def facebook_required(view):
  """
  Facebook login required decorator

  Checks to see if user's access token is valid. If not, the user is
  redirected to the URL specified in FACEBOOK_ERROR_REDIRECT. This is a layer
  of protection for Facebook-dependent pages. The user remains authenticated
  until (s)he logs out.
  """
  @wraps(view)
  def inner(request, *args, **kwargs):
    url = getattr(settings, 'FACEBOOK_ERROR_REDIRECT', '/')
    if request.user.is_authenticated():
      if valid_token(request.user):
        pass
      else:
        return HttpResponseRedirect(url)
    else:
      return HttpResponseRedirect(url)
    return view(request, *args, **kwargs)
  return inner
