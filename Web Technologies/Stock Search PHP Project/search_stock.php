<html>
<head>
<style type="text/css">
.mss
{
text-align:center
}
.news
{
margin-left:300;margin-right:300
}
.det
{
margin-left:300;margin-right:300
}
.tab
{
margin-left:300;margin-right:300
}
td
{
padding: 10 10 10 10
}
</style>


<script type="text/javascript">
function validate()
{		
	
	if (searchstock.symbol.value=="")
	{
	alert("Please enter a company symbol");
	return false;
	}
	return true;
}
	
</script>
</head>


<body>
<form name="searchstock" method="post" action="search_stock.php">
<div class="mss">
	<h1>Market Stock Search</h1>
	<div class="details">
		<label class="cs">Company Symbol:</label>
		<input class="sym" type="text" name="symbol" id="symbol">
		<input type="submit" name="submit" value="Search" onclick="validate()">
	</div>
	<div>
		<label class="example">Example:GOOG,MSFT,YHOO,FB,AAPL, ...etc</label>
	</div>
</div>
</form>

<?php 

$submitpressed=(isset($_POST['submit'])&&($_POST['symbol'])!="");
if(isset($_POST['submit'])&&($_POST['symbol'])!="")
{
	check();
}


function check()
{
	$url = "http://query.yahooapis.com/v1/public/yql?q=Select%20Name%2C%20Symbol%2C%20LastTradePriceOnly%2C%20Change%2C%20ChangeinPercent%2C%20PreviousClose%2C%20DaysLow%2C%20DaysHigh%2C%20Open%2C%20YearLow%2C%20YearHigh%2C%20Bid%2C%20Ask%2C%20AverageDailyVolume%2C%20OneyrTargetPrice%2C%20MarketCapitalization%2C%20Volume%2C%20Open%2C%20YearLow%20from%20yahoo.finance.quotes%20where%20symbol%3D%22".$_POST["symbol"]."%22&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
	$newsurl ="http://feeds.finance.yahoo.com/rss/2.0/headline?s=".$_POST['symbol']."&region=US&lang=en-US";
	$data = @simplexml_load_file($url) or die('<h1>Zero Results Found</h1>');
	//print_r ($data);
	$newsdata = @simplexml_load_file($newsurl) or die('<h1>Zero Results Found</h1>');
	$s = new XMLPHP;

	$s->test($data,$newsdata);
}
 class XMLPHP
{
	public $url;
	public $xml;
	public $results;
	public $quote;
	public $bid;
	public $change;
	public $dayslow;
	public $dayshigh;	
	public $yearlow;
	public $yearhigh;
	public $marketcap;
	public $lastprice;
	public $name;
	public $open;
	public $prevclose;
	public $changepercent;
	public $symbol;
	public $targetprice;
	public $volume;
	public $ask;
	public $avgvolume;

	public function test($data,$newsdata)
	{
		$xml=$data;
		$results= $xml->children();
		$quote=$results[0]->children();
		$bid=$xml->results->quote->Bid;
		$change=$xml->results->quote->Change;
		$dayslow= $xml->results->quote->DaysLow;
		$dayshigh=$xml->results->quote->DaysHigh;
		$yearlow=$xml->results->quote->YearLow;
		$yearhigh=$xml->results->quote->YearHigh;
		$marketcap=$xml->results->quote->MarketCapitalization;
		$lastprice=$xml->results->quote->LastTradePriceOnly;
		$name=$xml->results->quote->Name;
		$open=$xml->results->quote->Open;
		$prevclose=$xml->results->quote->PreviousClose;
		$changepercent=$xml->results->quote->ChangeinPercent;
		$symbol=$xml->results->quote->Symbol;
		$targetprice=$xml->results->quote->OneyrTargetPrice;
		$volume=$xml->results->quote->Volume;
		$ask=$xml->results->quote->Ask;
		$avgvolume=$xml->results->quote->AverageDailyVolume;

		$img1="http://www-scf.usc.edu/~csci571/2014Spring/hw6/down_r.gif";
		$img2="http://www-scf.usc.edu/~csci571/2014Spring/hw6/up_g.gif";

		$xmlnews=$newsdata;

		if($name=='N/A'&&$symbol=='N/A'&&$lastprice=='N/A'&&$change=='N/A'&&$changepercent=='N/A'&&$prevclose=='N/A'&&$dayslow=='N/A'&&$dayshigh=='N/A'&&$open=='N/A'&&$yearlow=='N/A'&&$yearhigh=='N/A'&&$bid=='N/A'&&$volume=='N/A'&&$ask=='N/A'&&$avgvolume=='N/A'&&$targetprice=='N/A'&&$marketcap=='N/A')
		{	return ;
		} elseif ($change<=0)
		{
			$this->viewHTML($name,$symbol,$lastprice,$change,$changepercent,$prevclose,$dayslow,$dayshigh,$open,$yearlow,$yearhigh,$bid,$volume,$ask,$avgvolume,$targetprice,$marketcap,$img1,$xmlnews);
		}
	else
	{
		$this->viewHTML($name,$symbol,$lastprice,$change,$changepercent,$prevclose,$dayslow,$dayshigh,$open,$yearlow,$yearhigh,$bid,$volume,$ask,$avgvolume,$targetprice,$marketcap,$img2,$xmlnews);
	}
}

	public function viewHTML($name,$symbol,$lastprice,$change,$changepercent,$prevclose,$dayslow,$dayshigh,$open,$yearlow,$yearhigh,$bid,$volume,$ask,$avgvolume,$targetprice,$marketcap,$img,$xmlnews)
	{	
		if($change==0.00)
		{	
			echo '<center><h1>Stock Information Not Available</h1>';
			return;
		} else{
			echo '<html><center><head><h4>Search Results</h4></head></center><body>';
			echo '<div class="main">';
			echo '<div class="det">';
			echo '<b>'.$name.'('.$symbol.')</b>&nbsp&nbsp&nbsp&nbsp&nbsp'.number_format((float)$lastprice,2,'.',',');
			$abschange=abs((float)$change);
			$abspercent=abs((float)$changepercent);
			if($change<0)
				echo '&nbsp&nbsp&nbsp&nbsp&nbsp<img src='.$img.'><font color="red">'.number_format((float)$abschange,2,'.','').'('.number_format((float)$abspercent,2,'.','').'%)</font>';
			else
				echo '&nbsp&nbsp&nbsp&nbsp&nbsp<img src='.$img.'><font color="green">'.number_format((float)$abschange,2,'.',',').'('.number_format((float)$abspercent,2,'.',',').'%)</font>';

		echo '<hr></div>';
		echo '<div class="tab">';
		echo '<table>';
		echo '<tr>';
		echo '<td  width="20%">Prev Close:</td>';
		echo '<td align="right" width="20%">'.number_format((float)$prevclose,2,'.',',').'</td>';
		echo "<td  width='20%'>Day's Range:</td>";
		echo '<td align="right" width="20%">'.number_format((float)$dayslow,2).'-'.number_format((float)$dayshigh,2).'</td>';
		echo '</tr><tr>';
		echo '<td  width="20%">Open:</td>';
		echo '<td align="right" width="20%">'.number_format((float)$open,2,'.',',').'</td>';
		echo "<td  width='20%'>52wk Range:</td>";
		echo '<td align="right" width="20%">'.number_format((float)$yearlow,2,'.',',').'-'.number_format((float)$yearhigh,2,'.',',').'</td>';
		echo '</tr><tr>';
		echo '<td>Bid:</td>';
		echo '<td align="right">'.number_format((float)$bid,2,'.',',').'</td>';
		echo '<td>Volume:</td>';
		echo '<td align="right">'.number_format((float)$volume,0).'</td>';
		echo '</tr><tr>';
		echo '<td>Ask:</td>';
		echo '<td align="right">'.number_format((float)$ask,2,'.',',').'</td>';
		echo '<td>Avg Vol(3m):</td>';
		echo '<td align="right">'.number_format((float)$avgvolume,0).'</td>';
		echo '</tr><tr>';
		echo '<td>1y Target Est:</td>';
		echo '<td align="right">'.number_format((float)$targetprice,2,'.',',').'</td>';
		echo '<td>Market Cap:</td>';
		echo '<td align="right">'.$marketcap.'</td>';
		echo '</tr></table></div>	';
		echo '<div class="news"';
		echo '<h4><b>News Headlines</b></h4><hr>';
		echo '<ul>';
	
		foreach($xmlnews->channel[0]->item as $list)
		{
			echo '<li><a href='.$list->link.' target="_blank">'.htmlentities($list->title,ENT_QUOTES,'UTF-8').'</a></li>';
		}
		echo '</ul>';
		echo '</div></div>';
		echo '</body></html>';
		}
	}
}

?>
</body>
</html>
