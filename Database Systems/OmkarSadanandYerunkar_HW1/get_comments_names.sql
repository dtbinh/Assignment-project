select distinct u1.first_name, u1.last_name
from users u, users u1, group1 g, postshare p, commenthas co
where g.abbrev='USC' and g.g_id=p.sender_id and p.share_type='public'and p.p_id= co.p_id and co.sender_id= u1.u_id and p.datetime>to_date('01/01/2014','mm/dd/yyyy')and p.datetime<to_date('01/31/2014','mm/dd/yyyy');