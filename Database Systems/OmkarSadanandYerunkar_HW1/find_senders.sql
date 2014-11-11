select u1.first_name,u1.last_name,count(co.sender_id)as NoOfComments
from users u,postshare p,commenthas co,users u1
where u.u_id=p.reciever_id and p.p_id=co.p_id and co.sender_id= u1.u_id and u.first_name='Jackie'and u.last_name='Chan'
group by u1.first_name,u1.last_name
having count(co.sender_id)>2