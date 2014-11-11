select distinct p.p_content
from postshare p, users u, resourceattach ra
where u.first_name='Jackie'and u.last_name='Chan'and p.sender_id= u.u_id and (p.datetime between to_date('01/01/2014','mm/dd/yyyy')and to_date('01/31/2014','mm/dd/yyyy'))and ra.p_id!= p.p_id;