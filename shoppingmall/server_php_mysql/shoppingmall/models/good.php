<?php  
	class good extends AppModel{  
		   var $name = 'good';
		   var $useTable = 'goods';
		   
		public function QueryGoodsList($cat_id, $sort_type=0)
		{
			$GoodsList = array();
			
			switch($sort_type)
			{
				//上架时间(默认)
				case 0:
					$sql="select * from lmy_goods where cat_id = ".$cat_id." ORDER BY `add_time` DESC";
					//$sql="select * from lmy_goods where cat_id = ".$cat_id."";
					break;
					
				//价格由低到高
				case 1:
					//$sql="select * from lmy_goods where cat_id = ".$cat_id."";
					$sql="select * from lmy_goods where cat_id = ".$cat_id." ORDER BY `shop_price` ASC";
					break;
				
				//价格由高到低
				case 2:
					//$sql="select * from lmy_goods where cat_id = ".$cat_id."";
					$sql="select * from lmy_goods where cat_id = ".$cat_id." ORDER BY `shop_price` DESC";
					break;
				
				//更新时间
				case 3:
					//$sql="select * from lmy_goods where cat_id = ".$cat_id."";
					$sql="select * from lmy_goods where cat_id = ".$cat_id." ORDER BY `last_update` DESC";
					break;
					
				default:
					//$sql="select * from lmy_goods where cat_id = ".$cat_id."";
					
					$sql="select * from lmy_goods where cat_id = ".$cat_id." ORDER BY `add_time` DESC ";
					break;
			}
			
			$GoodsList_rs=mysql_query($sql);
			
			$i = 0;
			while($record=mysql_fetch_assoc($GoodsList_rs))
			{	
				//判断商品是否已经删除进入回收站
				if($record['is_delete'])
				{
					continue;
				} 

				$temp_array = array('goods_id'=>$record['goods_id'], 'goods_name'=>$record['goods_name'], 'market_price'=>$record['market_price'], 'shop_price'=>$record['shop_price'], 'original_img'=>$record['goods_thumb']);
				$GoodsList[$i]=$temp_array;
				$i++;
			}
			
			return $GoodsList;
			
		}
		
		/*
		（1）goods_id ——商品编号。用户查询商品详细信息。
		（2）img_list商品图片为多张滚动效果（效果参考京东商城的设计），在V1.0版本中只显示一张图片。
		（3）shop_price  ——商城价格(优惠价格)
		（4）market_price ——市场价格(全价)
		（5）goods_name ——商品名
		（6）goods_number ——库存量
		*/
		public function QueryGoodsInfo($goods_id)
		{

			$sql="select * from lmy_goods where goods_id = ".$goods_id."";
			
			$GoodsInfo_result=mysql_query($sql);
			
			$GoodsInfo=null;
			while($record=mysql_fetch_assoc($GoodsInfo_result))
			{	
				//判断商品是否已经删除进入回收站
				if($record['is_delete'])
				{
					continue;
				} 

				$GoodsInfo = array('goods_id'=>$record['goods_id'], 'img_list'=>$record['goods_thumb'], 
				'shop_price'=>$record['shop_price'], 'market_price'=>$record['market_price'], 
				'goods_name'=>$record['goods_name'], 'goods_number'=>$record['goods_number']);

			}
			
			return $GoodsInfo;
		}

		public function QueryGoodsHtmlDesc($goods_id)
		{

			$GoodsDesc = array();
			$sql="select * from lmy_goods where goods_id = ".$goods_id."";
			
			$GoodsDesc_result=mysql_query($sql);
			
			$i = 0;
			while($record=mysql_fetch_assoc($GoodsDesc_result))
			{	
				//判断商品是否已经删除进入回收站
				if($record['is_delete'])
				{
					continue;
				}

				$temp_array = $record['goods_desc'];
				$GoodsDesc=$temp_array;
				$i++;
			}
			
			return $GoodsDesc;
			
		}
		
		
	}
?>  
