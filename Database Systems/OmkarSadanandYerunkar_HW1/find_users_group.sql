select distinct u.u_id,u.first_name,u.last_name,count(conn.touser)
from users u, postshare p, groupjoin gj, connections conn, users u1
where (u.u_id= p.sender_id and (p.datetime not in ( sysdate-7))) and gj.g_id= conn.touser and p.sender_id= conn.fromuser and u1.u_id= conn.fromuser and conn.conn_type='group'
group by u.u_id, u.first_name, u.last_name, conn.touser
having count(conn.touser)>10
