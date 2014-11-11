Select DISTINCT r."REVIEWER",r."BOOK_TITLE"
from REVIEWS r,BOOKS b,
XMLTABLE('for $review in /review return $review '
          passing r.Request1
          COLUMNS 
          "REVIEWER" varchar(50) PATH 'REVIEWER',
          "BOOK_TITLE" varchar(50) PATH 'BOOK_TITLE') r
Order By r."REVIEWER",r."BOOK_TITLE" ASC