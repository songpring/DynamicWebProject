<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Contactus_ko extends CI_Controller {

	function __construct() {
		parent::__construct();


	}

	public function index()	{
		$this->load->helper('form');		
		$this->load->view('contactus_ko/form');
	}


	public function register() {
		if(!$this->input->post('name', TRUE)) header('Location: /contactus_ko');

		$this->load->model('contactus_model');
		$arr_key = array('name', 'nationality', 'email', 'phonenumber','birthday','sns', 'instagram','uploadfiles');
		$extends_var = '';
		foreach($_POST as $key => $val) {
			if(!in_array($key, $arr_key)) {
				$extends_var[$key] = $this->input->post($key);
			}
		}

		$data = array(
			'name' => $this->input->post('name', TRUE),
			'nationality' => $this->input->post('nationality', TRUE),
			'email' => $this->input->post('email', TRUE),
			'phonenumber' => $this->input->post('phonenumber', TRUE),
			'birthday' => date('Y-m-d', strtotime($this->input->post('birthday', TRUE))),
			'sns' => $this->input->post('sns', TRUE),
			'instagram' => $this->input->post('instagram', TRUE),
			'uploadfiles' => $this->input->post('uploadfiles', TRUE),
			'extends_var' => json_encode($extends_var),
			'reg_datetime' => date('Y-m-d H:i:s'),	
			'reg_ip' => $this->input->ip_address()	
		);

		$result = $this->contactus_model->do_register($data);

		$data['result'] = $result;
		header('Location: /contactus_ko/complete');
		//$this->load->view('contactus/complete', $data);
	}

	public function complete() {
		$this->load->view('contactus_ko/complete');
	}

	public function view() {

		if (!$this->session->has_userdata('logged_in')) {
		  header('Location: /auth');
		  exit; 
		}

		$this->load->view('contactus_ko/list');
	}

	public function list_data() {

		if (!$this->session->has_userdata('logged_in')) {
		  header('Location: /auth');
		  exit; 
		}		
		
		$this->load->model('contactus_model');

		$limit = $this->input->get('limit', TRUE);
		$offset = $this->input->get('offset', TRUE);
		$order = $this->input->get('order', TRUE);
		$sort = $this->input->get('sort', TRUE);

		$limit = isset($limit) ? intval($limit) : 10;
		$offset = isset($offset) ? intval($offset) : 0;
		$order = isset($order) ? $order : 'asc';
		$sort = isset($sort) ? $sort : 'id'; // 기본설정 

		$data = array(
			'limit' => $limit,
			'offset' => $offset,
			'order' => $order,
			'sort' => $sort,
			'search' => $this->input->get('search', TRUE),
			'oper' => $this->input->post('oper', TRUE),	
			'no' => $this->input->post('no', TRUE),	
			'selectedCate' => $this->input->get('selectedCate', TRUE),	
			'cate1' => $this->input->post('cate1', TRUE)
		);

		$result = $this->contactus_model->do_listData($data);

		echo $result;
	}

	public function set() {

		if (!$this->session->has_userdata('logged_in')) {
		  header('Location: /auth');
		  exit; 
		}

		$user_data = $this->session->userdata('logged_in');

		// 최고 관리자만 접근
		if( $user_data['level'] < 9 ) {
			header('Location: /');
		  	exit;
		}
		
		$this->load->view('contactus_ko/set-form');
	}

	public function setlist_data() {

		if (!$this->session->has_userdata('logged_in')) {
		  header('Location: /auth');
		  exit; 
		}		
		
		$this->load->model('setform_model');

		$limit = $this->input->get('limit', TRUE);
		$offset = $this->input->get('offset', TRUE);
		$order = $this->input->get('order', TRUE);
		$sort = $this->input->get('sort', TRUE);

		$limit = isset($limit) ? intval($limit) : 10;
		$offset = isset($offset) ? intval($offset) : 0;
		$order = isset($order) ? $order : 'asc';
		$sort = isset($sort) ? $sort : 'id'; // 기본설정 

		$data = array(
			'limit' => $limit,
			'offset' => $offset,
			'order' => $order,
			'sort' => $sort,
			'search' => $this->input->get('search', TRUE),
			'oper' => $this->input->post('oper', TRUE),	
			'no' => $this->input->post('no', TRUE),	
			'tablename' => 'ci_contactus',
			'var_use' => $this->input->post('var_use', TRUE),	
			'var_type' => $this->input->post('var_type', TRUE),	
			'var_name' => $this->input->post('var_name', TRUE),	
			'var_values' => $this->input->post('var_values', TRUE),	
			'var_require' => $this->input->post('var_require', TRUE)			
		);

		$result = $this->setform_model->do_listData($data);

		echo $result;
	}

	public function get_form_data() {

		$this->load->model('setform_model');
		$data = array(
			'tablename' => 'ci_contactus'
		);
		$result = $this->setform_model->get_form_data($data);

		echo $result;		
	}


}
