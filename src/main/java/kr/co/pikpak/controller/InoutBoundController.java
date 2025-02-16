package kr.co.pikpak.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletResponse;
import kr.co.pikpak.dto.accepted_order_enroll_dto;
import kr.co.pikpak.dto.deliver_return_dto;
import kr.co.pikpak.dto.ex_receiving_dto;
import kr.co.pikpak.dto.ex_receiving_joined_dto;
import kr.co.pikpak.dto.input_request_dto;
import kr.co.pikpak.dto.order_enroll_dto_lhwtemp;
import kr.co.pikpak.dto.outgoing_enroll_dto;
import kr.co.pikpak.dto.outgoing_info_joined_dto;
import kr.co.pikpak.dto.outgoing_select_view_dto;
import kr.co.pikpak.dto.product_dto_lhwtemp;
import kr.co.pikpak.dto.receiving_dto;
import kr.co.pikpak.dto.supplier_info_dto_lhwtemp;
import kr.co.pikpak.dto.warehouse_locations_dto_lhwtemp;
import kr.co.pikpak.service.InoutBoundService;

@Controller
public class InoutBoundController {

	PrintWriter pw = null;

	@Autowired
	InoutBoundService ioservice;

	@Resource(name = "ir_dto")
	input_request_dto irdto;

	// 출고등록 데이터 삭제 => 차감한 거 다시 돌려야함
	@PostMapping("/inoutbound/delete_outenrollok")
	public String delete_outenrollok(ServletResponse res,
			@RequestParam(defaultValue = "", required = false) String[] each_ck_out,
			@RequestParam(defaultValue = "", required = false) String outenroll_cd,
			@RequestParam(defaultValue = "", required = false) String order_cd) {
		
		res.setContentType("text/html;charset=utf-8");

		try {
			this.pw = res.getWriter();
			// foreign 키 때문에 피킹을 먼저 삭제하고 등록을 삭제해야함
			// 출고피킹 정보 삭제 -> 삭제 되고 관련 수량 warehouse 복원 트리거 걸음
			int picking_result = ioservice.delete_outpiking(outenroll_cd);

			if (picking_result > 0) {
				// 출고등록정보 삭제
				int enroll_result = ioservice.delete_outenroll(outenroll_cd);

				// 주문승인테이블 '대기' 변경 (order_cd로 연결)
				int state_result = ioservice.update_accepted_back(order_cd);

				if (enroll_result > 0 && state_result > 0) {
					this.pw.print(
							"<script>" + "alert('정상적으로 삭제되었습니다.');" + "location.href = './outenroll';" + "</script>");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.pw.print(
					"<script>" + "alert('데이터베이스 문제로 삭제되지 못하였습니다.');" + "location.href = './outstate';" + "</script>");
		}

		return null;
	}

	// 출고확정
	@PostMapping("/inoutbound/decide_outgoingok")
	public String decide_outgoingok(ServletResponse res,
			@RequestParam(defaultValue = "", required = true) String outenroll_cd,
			@RequestParam(defaultValue = "", required = true) String order_cd,
			@RequestParam(defaultValue = "", required = true) String wh_warehouse_idx_datas[],
			@SessionAttribute(name = "activeUserID", required = false) String update_id) {
		res.setContentType("text/html;charset=utf-8");
			
		try {
			this.pw = res.getWriter();
			// outgoing_ernoll 상태 업데이트
			int out_state = ioservice.update_outenroll_decide(outenroll_cd, update_id);
			
			if (out_state > 0) { // 상태가 승인이 되면
				//삭제되어야할 재고 idx를 삭제시킴
				int w = 0;
				int delete_result = 0;
				int delete_count = 0;
				
				while(w < wh_warehouse_idx_datas.length) {
					String datas[] = wh_warehouse_idx_datas[w].split("&");
					if(datas[1].equals("Y")) {
						String wh_warehouse_idx = datas[0];
						delete_count++;
						//재고 전체 빠진거 삭제
						delete_result = ioservice.delete_warehouse_out(wh_warehouse_idx);	
					}
					w++;
				}
	
				
				if(delete_result == delete_count) { // 잘 delete 되면
					int order_result = ioservice.update_odstate_ended(order_cd);
					
					if(order_result > 0) {
						this.pw.print("<script>" + "alert('배송 확정이 완료되었습니다.');" + "location.href = './outstate';" + "</script>");
					}
					
				}
				
			}
			

		} catch (Exception e) {
			this.pw.print(
					"<script>" + "alert('데이터베이스 문제로 배송 확정에 실패했습니다.');" + "location.href = './outstate';" + "</script>");
		} finally {
			this.pw.close();
		}
		return null;
	}

	// 출고 등록
	@PostMapping("/inoutbound/outgoing_enrollok")
	public String outgoing_enrollok(ServletResponse res, 
			@ModelAttribute("outenroll") outgoing_enroll_dto dto,
			@RequestParam(defaultValue = "", required = true) String item_data,
			@SessionAttribute(name = "activeUserID", required = false) String operator_id) {
		res.setContentType("text/html;charset=utf-8");
		try {
			this.pw = res.getWriter();

			/* *****원래는 이걸 모듈로 빼야함***** */
			// 출고고유번호 생성
			String server_time = ioservice.get_time();
			
			int i = 0;
			String randnum = "";

			while (i < 4) {
				int pc = (int) (Math.ceil(Math.random() * 9));
				randnum += pc;
				i++;
			}

			String code = "OE-" + server_time + randnum;

			dto.setOutenroll_cd(code);

			// out_picking 데이터 생성
			List<Map<String, Object>> outgoing_picking = new ArrayList<Map<String, Object>>();

			String picking_data[] = item_data.split(",");
			int total_qty = 0; //위치별로 피킹한 것 전부 합친 수량
			
			int w = 0;

			int subtract_count = 0; //피킹등록 정보 전부 차감 됐는 지 확인하는 변수
			int type_count = 0; //로그기록 다 변경헀는지 확인하는 변수
			
			while (w < picking_data.length) { //로트번호 개수(피킹 개수)
				String datas[] = picking_data[w].split("&");
				total_qty += Integer.parseInt(datas[1]);

				Map<String, Object> outgoing_picking_data = new HashMap<>();
				outgoing_picking_data.put("out_enroll_cd", dto.getOutenroll_cd());
				outgoing_picking_data.put("wh_warehouse_idx", datas[2]);
				outgoing_picking_data.put("product_cd", dto.getProduct_cd());
				outgoing_picking_data.put("location_cd", datas[0]);
				outgoing_picking_data.put("receiving_cd", datas[3]);
				outgoing_picking_data.put("outpick_qty", datas[1]);
				outgoing_picking_data.put("is_deleted", datas[4]);
				w++;
				outgoing_picking.add(outgoing_picking_data);

				// 여기서 재고 차감 wh_warehouse_idx와 outpick_qty 활용해 쿼리문 실행
				int subtract_result = ioservice.update_warehouse_out(datas[1], operator_id, datas[2]);
				if (subtract_result > 0) {
					subtract_count++;
				}
				// 여기서 재고테이블 type변경
				int type_result = ioservice.update_stock_log_out(datas[2]);
				if(type_result > 0) {
					type_count++;
				}
			}

			dto.setTotal_qty(total_qty); //dto 에 총 수량 넣기

			// 출고정보 등록하는 쿼리 실행
			dto.setOperator_id(operator_id);
			
			int result = ioservice.insert_outgoing_enroll(dto);
			if (result > 0) { //출고등록이 성공하면
				// 출고피킹 등록하는 쿼리 실행
				int p_result = ioservice.insert_outgoing_picking(outgoing_picking); //이차배열로 넘겨줘서 mapper에선 foreach로 돌림
				
				if (p_result > 0) { //출고 피킹 등록이 성공하면
					// 상태도 변경
					int st_result = ioservice.update_acceptedorder_st(operator_id, dto.getOrder_cd());
					
					if (st_result > 0 && subtract_count == picking_data.length && type_count == picking_data.length) { //상태변경하고, 재고차감도 다 완료되고, 로그 타입 수정도 되면
						this.pw.print("<script>" + "alert('정상적으로 등록되었습니다.');" + "location.href = './outstate';"
								+ "</script>");
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			this.pw.print(
					"<script>" + "alert('데이터베이스 문제로 등록되지 못하였습니다.');" + "location.href = './outenroll';" + "</script>");
		} finally {
			this.pw.close();
		}
		return null;
	}

	// 출고등록 위치정보 가져오기
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping("/outgoing_locations")
	@ResponseBody
	public ResponseEntity<?> outgoing_locations(@RequestParam(defaultValue = "", required = true) String product_cd) {
		
		try {
			List<outgoing_select_view_dto> stock_info = ioservice.select_stock(product_cd);
			return ResponseEntity.ok(stock_info); // JSON으로 변환되어 전송
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
		}
	}

	// 입고 등록
	@PostMapping("inoutbound/inbound_enrollok")
	public String inbound_enrollok(ServletResponse res, @ModelAttribute("receiving") receiving_dto dto,
			@RequestParam(defaultValue = "", required = true) String total_qty,
			@RequestParam(defaultValue = "", required = false) String return_qty,
			@SessionAttribute(name = "activeUserID", required = false) String operator_id) {
		res.setContentType("text/html;charset=utf-8");
		// 넘어오는 값 : deliver_cd, exreceiving_cd, supplier_nm, supplier_cd, proudct_cd,
		// product_nm, receiving_qty, receiving_size, location_cd, inventory_dt,
		// receiving_log
		// 만들어야하는 값 : receiving_cd, lot_no, operator_id
		// 자동으로 들어가는 값 : receiving_idx, receiving_dt
		try {
			this.pw = res.getWriter();

			// 납품수량, 반품수량 계산
			dto.setReceiving_qty(Integer.parseInt(total_qty) - Integer.parseInt(return_qty));
			
			//세션
			dto.setOperator_id(operator_id);
			
			int result = ioservice.insert_receiving(dto);
			if (result > 0) {
				this.pw.print(
						"<script>" + "alert('정상적으로 등록 되었습니다.');" + "location.href = './recvenroll';" + "</script>");

			}

		} catch (Exception e) {
			e.printStackTrace();
			this.pw.print(
					"<script>" + "alert('데이터베이스 문제로 등록되지 못하였습니다.');" + "location.href = './recvenroll';" + "</script>");
		} finally {
			this.pw.close();
		}
		return null;
	}

	// 입고 모달 회사코드에 해당하는 위치코드 가져오기
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping("/inventory_locations")
	@ResponseBody
	public ResponseEntity<?> inventory_locations(@RequestParam(defaultValue = "", required = true) String supplier_cd) {
		try {
			List<warehouse_locations_dto_lhwtemp> locations = ioservice.select_locations(supplier_cd);
			return ResponseEntity.ok(locations); // JSON으로 변환되어 전송
		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
		}
	}

	// 납품 반송
	@PostMapping("/inoutbound/deliver_returnok")
	public String deliver_returnok(ServletResponse res, @ModelAttribute("deliver_return") deliver_return_dto dto,
			@SessionAttribute(name = "activeUserID", required = false) String operator_id) {
		res.setContentType("text/html;charset=utf-8");
		try {
			// 넘어오는 값 : deliver_cd, exreceiving_cd, d_return_type, d_return_dt, d_return_qty
			// 만들어야하는 값 : d_return_cd, operator_id
			// 자동 들어가는 값 : d_return_idx, d_enroll_dt
			this.pw = res.getWriter();
			
			//사용자 세션
			dto.setOperator_id(operator_id);
			
			int result = ioservice.insert_deliver_return(dto);
			if (result > 0) {
				this.pw.print("<script>" + "alert('정상적으로 등록되었습니다.');" + "location.href='./recvenroll';" + "</script>");
			}
		} catch (Exception e) {
			this.pw.print(
					"<script>" + "alert('데이터베이스 문제로 등록되지 못하였습니다.');" + "location.href='./recvenroll';" + "</script>");
			e.printStackTrace();
		} finally {
			this.pw.close();
		}
		return null;
	}

	// 상품 칮기 페이징 + 검색
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping("/product_paging")
	@ResponseBody
	public Map<String, Object> product_paging(@RequestParam(defaultValue = "", required = false) int page,
			@RequestParam(defaultValue = "", required = false) String pd_nm,
			@RequestParam(defaultValue = "", required = false) String pd_cd) {
	
		int page_size = 8; // 한 페이지당 보여줄 리스트 개수
		int startpg = (page - 1) * page_size;

		Map<String, Object> product = new HashMap<>();
		product.put("startpg", startpg); // 시작 페이지
		product.put("page_size", page_size); // 보여줄 리스트 개수
		product.put("pd_nm", pd_nm);
		product.put("pd_cd", pd_cd);

		Map<String, Object> response = new HashMap<>();

		try {
			List<product_dto_lhwtemp> pdlist_part = ioservice.select_product_limit(product);
			Integer pd_total = ioservice.select_product_total(pd_nm, pd_cd);

			response.put("pdlist_part", pdlist_part); // 페이징 리스트
			response.put("pd_total", pd_total); // 총 리스트 개수
			response.put("page_size", page_size); // 페이징 사이즈
		} catch (Exception e) {
			//System.out.println(e);
		}

		return response;
	}

	// 매입처 페이징 + 검색
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping("/company_paging") // 여기서는 그냥 그대로 경로 써주면 되는구나 ajax니까
	@ResponseBody
	public Map<String, Object> company_paging(@RequestParam(defaultValue = "", required = false) int page,
			@RequestParam(defaultValue = "", required = false) String comp_nm,
			@RequestParam(defaultValue = "", required = false) String comp_cd) {

		int page_size = 8; // 한 페이지당 보여줄 리스트 개수
		int startpg = (page - 1) * page_size;

		Map<String, Object> supplier = new HashMap<>();
		supplier.put("startpg", startpg);
		supplier.put("page_size", page_size);
		supplier.put("comp_nm", comp_nm);
		supplier.put("comp_cd", comp_cd);

		Map<String, Object> response = new HashMap<>();

		try {
			List<supplier_info_dto_lhwtemp> splist_part = ioservice.select_supplier_limit(supplier);

			Integer sp_total = ioservice.select_supplier_total(comp_nm, comp_cd);

			response.put("splist_part", splist_part); // 페이징 리스트
			response.put("sp_total", sp_total); // 총 리스트 개수
			response.put("page_size", page_size); // 페이징 사이즈
		} catch (Exception e) {
			//System.out.println(e);
		}

		return response;
	}

	// 입고요청 삭제
	@PostMapping("inoutbound/delete_inreqok")
	public String delete_inreqok(ServletResponse res,
			@RequestParam(defaultValue = "", required = false) String each_ck[],
			@RequestParam(defaultValue = "", required = false) String request_idx) {
		res.setContentType("text/html;charset=utf-8");
		String idx_datas[] = request_idx.split(",");

		if (idx_datas.length == each_ck.length) {

			try {
				this.pw = res.getWriter();
				int result = ioservice.delete_inreq(request_idx);
				if (result == idx_datas.length) {
					this.pw.print(
							"<script>" + "alert('정상적으로 삭제되었습니다.');" + "location.href = './inboundreq';" + "</script>");
				}
			} catch (Exception e) {
				this.pw.print("<script>" + "alert('데이터베이스 문제로 삭제되지 못하였습니다.');" + "location.href = './inboundreq';"
						+ "</script>");
			} finally {
				this.pw.close();
			}
		}
		return null;
	}

	// 입고요청 수정
	@PostMapping("inoutbound/inreq_modifyok")
	public String inreq_modifyok(ServletResponse res,
			@RequestParam(defaultValue = "", required = false) int product_qty,
			@RequestParam(defaultValue = "", required = false) String add_req,
			@RequestParam(defaultValue = "", required = false) String hope_dt,
			@RequestParam(defaultValue = "", required = false) String request_idx,
			@SessionAttribute(name = "activeUserID", required = false) String update_id) {

		res.setContentType("text/html;charset=utf-8");
		Map<String, Object> inrequest = new HashMap<>();
		inrequest.put("product_qty", product_qty);
		inrequest.put("add_req", add_req);
		
		inrequest.put("hope_dt", hope_dt);

		//activeUserID
		inrequest.put("update_id", update_id); // 업데이트 id

		String update_nm = ioservice.search_one_id(update_id);
	
		inrequest.put("request_idx", request_idx);
		try {
			this.pw = res.getWriter();
			int result = ioservice.update_inreq(inrequest);
			if (result > 0) {
				this.pw.print("<script>" + "alert('정상적으로 수정되었습니다.');" + "location.href='./inboundreq';" + "</script>");
			}

		} catch (Exception e) {
			this.pw.print(
					"<script>" + "alert('데이터베이스 문제로 수정되지 못하였습니다.');" + "location.href='./inboundreq';" + "</script>");
			
		} finally {
			this.pw.close();
		}
		
		return null;
	}

	// 입고요청 등록
	@PostMapping("inoutbound/inreq_enrollok")
	public String inreq_enrollok(@ModelAttribute("ir") input_request_dto dto, ServletResponse res,
			@SessionAttribute(name = "activeUserID", required = false) String operator_id) {
		res.setContentType("text/html;charset=utf-8");
		// System.out.println(dto.getProduct_nm());
		// 넘어온 값 : supplier_cd, supplier_nm, product_cd, product_nm, product_qty,
		// add_req, hope_dt
		// 안 넘어온 값(따로 처리해야하는 값) : request_cd, operator_id, operator_nm, request_st
		// 안 넘어와도 되는 값(쿼리문 처리 or null값): request_idx, request_dt, update_dt
		try {
			this.pw = res.getWriter();
			dto.setOperator_id(operator_id);
			
			int result = ioservice.input_req_insert(dto);

			if (result > 0) {
				this.pw.print("<script>" + "alert('정상적으로 등록되었습니다.');" + "location.href='./inboundreq';" + "</script>");
			}
		} catch (Exception e) {
			this.pw.print("<script>" + "alert('데이터베이스 문제로 인해 등록되지 못하였습니다.');" + "location.href='./inboundreq';"
					+ "</script>");
		} finally {
			this.pw.close();
		}
		return null;
	}

	// 입고요청 리스트 검색
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/inboundreq_search")
	public ResponseEntity<List<input_request_dto>> inboundreq_search(@RequestBody Map<String, Object> data_arr) {
		List<input_request_dto> ir_search = null;
		try {
			String operator_nm = data_arr.get("operator_nm").toString();
			// operator_nm을 operator_id로 가져오는 쿼리문을 작성하고 불러와야함
			List<String> op_id = ioservice.search_operator_nm(operator_nm);

			// 조회된 operator_id 리스트를 data_arr에 추가
			data_arr.put("operator_id_list", op_id);

			ir_search = ioservice.select_inreq_search(data_arr);
			return ResponseEntity.ok(ir_search);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

	}

	// 입고요청 이동
	@GetMapping("inoutbound/inboundreq")
	public String inboundreq(Model m) {

		// 등록 모달에 상품리스트 출력
		List<Map<String, Object>> pdlist = ioservice.select_product();
		m.addAttribute("pdlist", pdlist);

		// 입고요청 리스트 출력
		List<input_request_dto> ir_list = ioservice.select_inreq();
		m.addAttribute("ir_list", ir_list);


		return null;
	}

	//입고등록 서치 리스트
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/receiving_search")
	public ResponseEntity<List<ex_receiving_joined_dto>> receiving_search(
			@RequestBody Map<String, Object> data_arr){
		try {
			List<ex_receiving_joined_dto> exrecv_list = ioservice.select_ex_receiving(data_arr);
			
			return ResponseEntity.ok(exrecv_list);
		}
		catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	
	// 입고 등록 이동
	@GetMapping("inoutbound/recvenroll")
	public String recvenroll(Model m) {
		// 입고 모달에 위치코드 정보 불러오기

		// 가입고 리스트 불러오기
		//List<ex_receiving_joined_dto> exrecv_list = ioservice.select_ex_receiving();
		//m.addAttribute("exrecv_list", exrecv_list);

		return null;
	}

	//출고현황 검색
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/outstate_search")
	public ResponseEntity<List<outgoing_info_joined_dto>> outstate_search(
			@RequestBody Map<String, Object> data_arr){
		try {
			List<outgoing_info_joined_dto> out_info = ioservice.select_outgoing_view(data_arr);
			return ResponseEntity.ok(out_info);
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	
	// 출고현황 이동
	@GetMapping("inoutbound/outstate")
	public String outstate(Model m) {
		/*
		List<outgoing_info_joined_dto> out_info = ioservice.select_outgoing_view();
		
		ObjectMapper objectMapper = new ObjectMapper();
		out_info.forEach(dto -> {
			try {
				dto.setPickings_json(objectMapper.writeValueAsString(dto.getPickings()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		m.addAttribute("out_info", out_info);
		*/
		return null;
	}

	
	//출고등록 리스트 검색
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/outenroll_search")
	public ResponseEntity<List<accepted_order_enroll_dto>> outenroll_search(
			@RequestBody Map<String, Object> data_arr){
		try {
			List<accepted_order_enroll_dto> orderlist = ioservice.select_order_enroll(data_arr);
			return ResponseEntity.ok(orderlist);
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	
	// 출고 등록 이동
	@GetMapping("inoutbound/outenroll")
	public String outenroll(Model m) {
	
		return null;
	}

	
}
