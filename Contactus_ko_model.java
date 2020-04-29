<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Contactus_ko_model extends CI_Model {

	public $tablename = 'ci_contactus';

	public function __construct() {
		parent::__construct();
	}


	public function do_listData($data) {

		// insert, update , delete
		if(!empty($data['oper'])) {

			$r_data = array();
			$r_data['result'] = 'success';

			if($data['oper']=='edit') {
				$array = array(
			        'cate' => $data['cate1']
				);

				$this->db->where('id', $data['no']);
				$this->db->update($this->tablename, $array);				
			}
			else if($data['oper']=='del') {
				$no_array = explode(',', $data['no']);

			    foreach($no_array as $key) {  // delete 1 

					$this->db->where('id', $key);
					$this->db->delete($this->tablename);
			    }

			}

		    $r_data['oper'] = $data['oper'];
		    echo json_encode($r_data);
		    exit;			
		
		}

		if(!empty($data['selectedCate'])) $this->db->where('cate', $data['selectedCate']);

		if(!empty($data['search'])) {
			$this->db->group_start();
			$array = array('name' => $data['search'], 'nationality' => $data['search'],'email' => $data['search'], 'phonenumber' => $data['search'], 'sns' => $data['search'],'instagram' => $data['search'], 'uploadfiles' => $data['search'], 'extends_var' => $data['search']);
			$this->db->or_like($array);
			$this->db->group_end();			
		}

		$this->db->from($this->tablename);

		$user_data = $this->session->userdata('logged_in');

		$responce = new stdClass();
		$responce->total = $this->db->count_all_results();	// flush_cache

		$this->db->from($this->tablename);

		if(!empty($data['search'])) {
			$this->db->group_start();
			$array = array('name' => $data['search'],'nationality' => $data['search'], 'email' => $data['search'], 'phonenumber' => $data['search'], 'sns' => $data['search'],'instagram' => $data['search'], 'uploadfiles' => $data['search'], 'extends_var' => $data['search']);
			$this->db->or_like($array);	
			$this->db->group_end();	
		}	

		if(!empty($data['selectedCate'])) $this->db->where('cate', $data['selectedCate']);

		$this->db->select(' id, cate, name, nationality, email, phonenumber, birthday, sns, instagram, uploadfiles, extends_var, reg_datetime ');


		//$this->db->where('level', 1);

		$this->db->order_by($data['sort'], $data['order']);
		$this->db->limit($data['limit'], $data['offset']);

		$query = $this->db->get();

		$responce->rows = array();
		//$responce->query = $this->db->last_query(); 

		$num = $responce->total - $data['offset'];//$offset + 1;
		foreach ($query->result() as $row) {

			$row->num = $num;
			$responce->rows[] = $row;

			$num--;
		}

		$this->db->flush_cache();
		$this->db->select(' cate ');
		$this->db->from($this->tablename);
		$this->db->where('cate !=', '');
		$this->db->group_by('cate');
		$query = $this->db->get();
		foreach ($query->result() as $row) {
			$responce->cates[] = $row;
		}


		return json_encode($responce);
	}	

	public function do_register($data) {

		$this->db->set($data);
		$this->db->insert($this->tablename);

		$lastid = $this->db->insert_id();

		return $lastid;
	}

	public function get_category() {

		$this->db->from($this->tablename);
		$query = $this->db->get();
	}
}