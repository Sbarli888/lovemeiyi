<?php  
class CategoryController extends AppController {  
     var $name = 'Category';  
   
     var $uses = array('category','good');

	 var $modelNames = array('category', 'good');
	 
	 var $autoRender = false;  //放弃使用View
	 
	 //var $view = 'Output';
	 
	 /*
	 public $components = array('RequestHandler');//这个不能缺少，否则不能把theme指向xml/json目录

	 public $helpers = array(
			'Xml',                  //for xml
			'Javascript',      //for json
		);
	 */

     public function Top() {
	 
	 		//调用Model完成业务逻辑处理
			$data_Top =array();
			$data_Top = $this->category->QueryTopCat();
			
			//调用View完成数据返回
			header("Content-Type:text/json; charset=utf-8");
			print_r(json_encode($data_Top));

     }
	 
	 public function Second($parent_id) {
	 
	 		//调用Model完成业务逻辑处理
			$data_Second =array();
			$data_Second = $this->category->QuerySecondCat($parent_id);
			
			//调用View完成数据返回
			header("Content-Type:text/json; charset=utf-8");
			print_r(json_encode($data_Second));
     }
	 
     public function Goods($cat_id, $sort_type=0) {
	 
	 		//调用Model完成业务逻辑处理
			
			$data_GoodsList =array();
			$data_GoodsList = $this->good->QueryGoodsList($cat_id, $sort_type);
			
			//调用View完成数据返回
			header("Content-Type:text/json; charset=utf-8");
			print_r(json_encode($data_GoodsList));
     }
}  
?>
