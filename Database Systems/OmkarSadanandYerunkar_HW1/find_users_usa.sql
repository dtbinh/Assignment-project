select distinct u1.u_id, u1.first_name, u1.last_name, u1.email
from users u1, users u, connections conn, groupjoin gj
where u.country='USA' and u1.country='USA'and u.u_id= gj.u_id and gj.g_id= conn.touser and conn.conn_type='group' and conn.fromuser= u1.u_id