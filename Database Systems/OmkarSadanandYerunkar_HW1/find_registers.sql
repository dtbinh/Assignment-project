select u.u_id,u.first_name,u.last_name,u.email,to_char(u.registration_date,'hh24:mi:ss') as Registration_Time
from users u
where u.registration_date>to_date('01/24/2013 00:00:00','mm/dd/yyyy hh24:mi:ss') and u.registration_date<to_date('01/24/2013 23:59:59','mm/dd/yyyy hh24:mi:ss')
order by to_char(u.registration_date,'hh24:mi:ss') DESC