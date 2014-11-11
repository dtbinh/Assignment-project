<?php 
if(($_GET['symbol'])!="")
{

check();
}


function check()
{
$url = "http://query.yahooapis.com/v1/public/yql?q=Select%20Name%2C%20Symbol%2C%20LastTradePriceOnly%2C%20Change%2C%20ChangeinPercent%2C%20PreviousClose%2C%20DaysLow%2C%20DaysHigh%2C%20Open%2C%20YearLow%2C%20YearHigh%2C%20Bid%2C%20Ask%2C%20AverageDailyVolume%2C%20OneyrTargetPrice%2C%20MarketCapitalization%2C%20Volume%2C%20Open%2C%20YearLow%20from%20yahoo.finance.quotes%20where%20symbol%3D%22".$_GET["symbol"]."%22&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
$newsurl ="http://feeds.finance.yahoo.com/rss/2.0/headline?s=".$_GET['symbol']."&region=US&lang=en-US";
$data = @simplexml_load_file($url);
$symbvalue = $_GET['symbol'];
$newsdata = @simplexml_load_file($newsurl,'SimpleXMLElement', LIBXML_NOCDATA);
$s = new XMLPHP;

$s->test($data,$newsdata,$symbvalue);
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



public function test($data,$newsdata,$symbvalue)
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
{return ;}
elseif ($change<=0)
{
$this->viewHTML($name,$symbol,$lastprice,$change,$changepercent,$prevclose,$dayslow,$dayshigh,$open,$yearlow,$yearhigh,$bid,$volume,$ask,$avgvolume,$targetprice,$marketcap,$img1,$xmlnews,$symbvalue);
}

else
{
$this->viewHTML($name,$symbol,$lastprice,$change,$changepercent,$prevclose,$dayslow,$dayshigh,$open,$yearlow,$yearhigh,$bid,$volume,$ask,$avgvolume,$targetprice,$marketcap,$img2,$xmlnews,$symbvalue);
}

}
public function viewHTML($name,$symbol,$lastprice,$change,$changepercent,$prevclose,$dayslow,$dayshigh,$open,$yearlow,$yearhigh,$bid,$volume,$ask,$avgvolume,$targetprice,$marketcap,$img,$xmlnews,$symbvalue)
{	
	header('Content-Type: text/xml');
    $xml_data ="<?xml version='1.0' encoding='UTF-8'?>";
	$xml_data .="<result>";
	$abschange=abs((float)$change);
	$abspercent=abs((float)$changepercent);
	$xml_data .="<Name>".$name."</Name>";
	$xml_data .="<Symbol>".$symbol."</Symbol>";
	$xml_data .="<Quote>";
	if($change<0)
	{
	$xml_data .= "<ChangeType>-</ChangeType>";
	}
	else 
	{
	$xml_data .= "<ChangeType>+</ChangeType>";
	}
	
	$xml_data .="<Change>".number_format((float)$abschange,2,'.','')."</Change>";
	$xml_data .="<ChangeInPercent>".number_format((float)$abspercent,2,'.','')."</ChangeInPercent>";
	$xml_data .="<LastTradePriceOnly>".number_format((float)$lastprice,2,'.',',')."</LastTradePriceOnly>";
	$xml_data .="<PreviousClose>".number_format((float)$prevclose,2,'.',',')."</PreviousClose>";
	$xml_data .="<DaysLow>".number_format((float)$dayslow,2)."</DaysLow>";
	$xml_data .="<DaysHigh>".number_format((float)$dayshigh,2)."</DaysHigh>";
	$xml_data .="<Open>".number_format((float)$open,2,'.',',')."</Open>";
	$xml_data .="<YearLow>".number_format((float)$yearlow,2,'.',',')."</YearLow>";
	$xml_data .="<YearHigh>".number_format((float)$yearhigh,2,'.',',')."</YearHigh>";
	$xml_data .="<Bid>".number_format((float)$bid,2,'.',',')."</Bid>";
	$xml_data .="<Volume>".number_format((float)$volume,0)."</Volume>";
	$xml_data .="<Ask>".number_format((float)$ask,2,'.',',')."</Ask>";
	$xml_data .="<AverageDailyVolume>".number_format((float)$avgvolume,0)."</AverageDailyVolume>";
	$xml_data .="<OneYearTargetPrice>".number_format((float)$targetprice,2,'.',',')."</OneYearTargetPrice>";
	$xml_data .="<MarketCapitalization>".$marketcap."</MarketCapitalization>";
	$xml_data .="</Quote>";
	$xml_data .="<News>";
	foreach($xmlnews->channel[0]->item as $list)
	{
		if($xmlnews->channel[0]->item->title[0]=="Yahoo! Finance: RSS feed not found")
		{
		$xml_data .="<Item>";
		$xml_data .="<Title>News Information Not Available</Title>";
		$xml_data .="<Link>".htmlspecialchars($list->link)."</Link>";
		$xml_data .="</Item>";
			break;
		}
		$xml_data .="<Item>";
		$xml_data .="<Title>".htmlspecialchars($list->title)."</Title>";
		$xml_data .="<Link>".htmlspecialchars($list->link)."</Link>";
		$xml_data .="</Item>";
	}

	$xml_data .="</News>";
	$xml_data .="<StockChartImageURL>http://chart.finance.yahoo.com/t?s=".$symbvalue."&amp;lang=en-US&amp;amp;width=300&amp;height=180</StockChartImageURL>";
	$xml_data .="</result>";
	print $xml_data;
	
	

	}
}


?>




