<?php  
	class article extends AppModel{  
		   var $name = 'article';
		   var $useTable = 'article';
		   
		public function QueryTips()
		{
			//ÁªºÏ²éÑ¯
			$sql = "SELECT `title`, `content` FROM `lmy_article` WHERE `cat_id` = 7 ";
			// (SELECT `cat_id` FROM `lmy_article_cat` WHERE `cat_name` = \'mobile_goods_tips\' LIMIT 0 , 30)"
			
			$Tips_result=mysql_query($sql);
			
			$GoodsTips=array();
			$i = 0;
			while($record=mysql_fetch_assoc($Tips_result))
			{	
				$temp_array = array('title'=>$record['title'], 'content'=>$record['content']);
				$GoodsTips[$i]=$temp_array;
				$i++;
			}

			return array('tips'=>$GoodsTips);
			
		}
	}
?>  
