<?php  
	class shop_config extends AppModel{  
		   var $name = 'shop_config';
		   var $useTable = 'shop_config';
		   
		public function QueryContact()
		{
			$sql="select * from lmy_shop_config where code = 'service_phone'";
			$phone_result=mysql_query($sql);
			$record1=mysql_fetch_assoc($phone_result);

			$sql="select * from lmy_shop_config where code = 'qq'";
			$qq_result=mysql_query($sql);
			$record2=mysql_fetch_assoc($qq_result);
			
			$Contact = array('service_phone'=>$record1['value'], 'qq'=>$record2['value']);

			return $Contact;
			
		}
	}
?>  
