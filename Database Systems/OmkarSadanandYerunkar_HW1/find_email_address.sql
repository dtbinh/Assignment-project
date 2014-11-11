select distinct u1.email
from users u, connections conn, users u1
where ((u.first_name='Jackie' and u.last_name='Chan')or (u.first_name='Lady' and u.last_name='Gaga')) and u.u_id= conn.touser and conn.fromuser= u1.u_id