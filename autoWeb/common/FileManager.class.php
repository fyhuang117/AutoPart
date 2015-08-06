<?php
class FileManager{
	
	private $file_name = NULL;
	private $file_path = NULL;
	private $file_data = NULL;
	private $error_message = NULL;
	
	function __set($n,$v){
		$this->$n = $v;
	}
	
	function __get($n){
		return $this->$n;
	}
	
	/**
	 * 
	 * @param $_FILES $file
	 * @param string $save_path
	 * @param string $disc_path
	 * @return boolean
	 */
	public function upload($file,$file_path,$disc_path){
		if($this->checkUploadError($file)){
			if(!empty($file["name"])){
				//处理上传文件
				$new_file_name = $this->getNewFileName();
				$ext = $this->getFileExt($file["name"]);
				$this->file_name = $new_file_name.'.'.$ext;
				
				$file_save_path = $disc_path.$file_path;
				
				if(!is_dir($file_save_path)){
					mkdir($file_save_path);
				}
				
				$this->file_path = $file_path;
				
				move_uploaded_file($file["tmp_name"],$file_save_path.$this->file_name);
				
				return TRUE;
			}
		}else{
			return FALSE;
		}
	}
	
	/**
	 * 检查上传中是否有错误
	 * @param $_FILES $file
	 * @return boolean
	 */
	private function checkUploadError($file){
		if($file["error"] != 0){
			$error = $file["error"];
			switch ($error){
				case 1:
					$this->error_message = '文件大小超过限制';
					break;
				case 2:
					$this->error_message = '文件大小超过限制';
					break;
				case 3:
					$this->error_message = '上传不完整';
					break;
				case 4:
					$this->error_message = '找不到文件';
					break;
				default:
					$this->error_message = '未知错误';
			}
			return FALSE;
		}else{
			return TRUE;
		}
	}
	
	/**
	 * 获得新文件名
	 * @return number
	 */
	private function getNewFileName(){
		return gmmktime(idate('H'),idate('i'),idate('s'),idate('m'),idate('d'),idate('Y'));
	}
	
	/**
	 * 获得原后缀名
	 * @param string $filename
	 * @return Ambigous <>|string
	 */
	private function getFileExt($filename){
		$str_array = explode('.', $filename);
		if(count($str_array) > 0){
			return $str_array[count($str_array) - 1];
		}else{
			return '';
		}
	}
	
	/**
	 * 获取文件数据
	 * @return string|NULL
	 */
	public function getFileData($disc_path){
		$file_path_str = $disc_path.$this->file_path.$this->file_name;
		if(is_file($file_path_str)){
			$filesize = filesize($file_path_str);
			$this->file_data = fread(fopen($file_path_str, "r"), $filesize);
		}
	}
	
	/**
	 * 删除文件
	 * @param string $disc_path
	 */
	public function delFile($disc_path){
		if(is_file($disc_path.$this->file_path.$this->file_name)){
			unlink($disc_path.$this->file_path.$this->file_name);
		}
	}
}
?>