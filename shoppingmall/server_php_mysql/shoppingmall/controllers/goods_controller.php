<?php  
class GoodsController extends AppController {  
     var $name = 'Goods';  
   
	 var $uses = array('good', 'article', 'shop_config');

	 var $modelNames = array('good', 'article', 'shop_config');
	 
	 var $autoRender = false;  //放弃使用View
	 
	 
     public function desc($goods_id) {
	 
	 		//调用Model完成业务逻辑处理
			$data_GoodsDesc =array();
			$data_GoodsDesc = $this->good->QueryGoodsHtmlDesc($goods_id);
			
			//调用View完成数据返回
			header("Content-Type:text/html; charset=utf-8");
			print_r($data_GoodsDesc);

     }
	 
	 public function info($goods_id) {
	 
	 		//调用Model完成业务逻辑处理
			$data_GoodsInfo =array();
			
			//在goods表查询6项商品信息
			$data_GoodsBasicInfo = $this->good->QueryGoodsInfo($goods_id);
			if($data_GoodsBasicInfo == null)
			{
				header("Content-Type:text/json; charset=utf-8");
				print_r(json_encode($data_GoodsInfo));
				return ;
			}
			
			//在article表中查询tips信息
			$data_GoodsTips = $this->article->QueryTips();
			
			//在shop_config表中查询客服联系方式信息
			$data_GoodsContact = $this->shop_config->QueryContact();
			
			//合并数据
			$data_GoodsInfo = array_merge($data_GoodsBasicInfo, $data_GoodsTips, $data_GoodsContact);
			
			//调用View完成数据返回
			header("Content-Type:text/json; charset=utf-8");
			print_r(json_encode($data_GoodsInfo));
			//print_r($data_GoodsInfo);

     }
}  
?>
