<?php  
	class category extends AppModel{  
		   var $name = 'category';
		   var $useTable = 'category';
		public function QueryTopCat()
		{
			$TopCat = array();
			$sql="select * from lmy_category where parent_id = 0";
			
			$TopCat_result=mysql_query($sql);
			//$sqlpage="select * from ly where sh=1 order by id desc limit ".($page*$pagenum).",$pagenum ";
			
			$i = 0;
			while($record=mysql_fetch_assoc($TopCat_result))
			{	
				$temp_array = array('cat_id'=>$record['cat_id'], 'cat_name'=>$record['cat_name']);
				$TopCat[$i]=$temp_array;
				$i++;
			}
			
			return $TopCat;
			
		}
		
		
		public function QuerySecondCat($parent_id)
		{
			$SecondCat = array();
			$sql="select * from lmy_category where parent_id = ".$parent_id."";
			
			$SecondCat_result=mysql_query($sql);
			//$sqlpage="select * from ly where sh=1 order by id desc limit ".($page*$pagenum).",$pagenum ";
			
			$i = 0;
			while($record=mysql_fetch_assoc($SecondCat_result))
			{	
				$temp_array = array('cat_id'=>$record['cat_id'], 'cat_name'=>$record['cat_name']);
				$SecondCat[$i]=$temp_array;
				$i++;
			}
			
			return $SecondCat;
			
		}
	}
?>  
