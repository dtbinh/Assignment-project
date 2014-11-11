Select u.first_name,u.last_name
from Users u, connections conn,Group1 g
where u.u_id= conn.fromuser and g.abbrev='USC'and g.g_id= conn.touser;