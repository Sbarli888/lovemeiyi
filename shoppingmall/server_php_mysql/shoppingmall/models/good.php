<?php  
	class good extends AppModel{  
		   var $name = 'good';
		   var $useTable = 'goods';
		   
		public function QueryGoodsList($cat_id, $sort_type=0)
		{
			$GoodsList = array();
			
			switch($sort_type)
			{
				//�ϼ�ʱ��(Ĭ��)
				case 0:
					$sql="select * from lmy_goods where cat_id = ".$cat_id." ORDER BY `add_time` DESC";
					//$sql="select * from lmy_goods where cat_id = ".$cat_id."";
					break;
					
				//�۸��ɵ͵���
				case 1:
					//$sql="select * from lmy_goods where cat_id = ".$cat_id."";
					$sql="select * from lmy_goods where cat_id = ".$cat_id." ORDER BY `shop_price` ASC";
					break;
				
				//�۸��ɸߵ���
				case 2:
					//$sql="select * from lmy_goods where cat_id = ".$cat_id."";
					$sql="select * from lmy_goods where cat_id = ".$cat_id." ORDER BY `shop_price` DESC";
					break;
				
				//����ʱ��
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
				//�ж���Ʒ�Ƿ��Ѿ�ɾ���������վ
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
		��1��goods_id ������Ʒ��š��û���ѯ��Ʒ��ϸ��Ϣ��
		��2��img_list��ƷͼƬΪ���Ź���Ч����Ч���ο������̳ǵ���ƣ�����V1.0�汾��ֻ��ʾһ��ͼƬ��
		��3��shop_price  �����̳Ǽ۸�(�Żݼ۸�)
		��4��market_price �����г��۸�(ȫ��)
		��5��goods_name ������Ʒ��
		��6��goods_number ���������
		*/
		public function QueryGoodsInfo($goods_id)
		{

			$sql="select * from lmy_goods where goods_id = ".$goods_id."";
			
			$GoodsInfo_result=mysql_query($sql);
			
			$GoodsInfo=null;
			while($record=mysql_fetch_assoc($GoodsInfo_result))
			{	
				//�ж���Ʒ�Ƿ��Ѿ�ɾ���������վ
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
				//�ж���Ʒ�Ƿ��Ѿ�ɾ���������վ
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
