select p.p_content
from postshare p, users u
where u.first_name='Jackie'and u.last_name='Chan'and p.sender_id= u.u_id and p.datetime > to_date('01/01/2014','mm/dd/yyyy');