package com.gaji.SingleBungle.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gaji.SingleBungle.admin.service.adminService;
import com.gaji.SingleBungle.admin.vo.AAttachment;
import com.gaji.SingleBungle.admin.vo.ABoard;
import com.gaji.SingleBungle.admin.vo.APageInfo;
import com.gaji.SingleBungle.admin.vo.IAttachment;
import com.gaji.SingleBungle.admin.vo.Reply;
import com.gaji.SingleBungle.admin.vo.inquiry;
import com.gaji.SingleBungle.admin.vo.reportBoard;
import com.gaji.SingleBungle.admin.vo.reportReply;
import com.gaji.SingleBungle.board.model.service.BoardService;
import com.gaji.SingleBungle.board.model.vo.Board;
import com.gaji.SingleBungle.board.model.vo.BoardAttachment;
import com.gaji.SingleBungle.cafe.model.service.CafeService;
import com.gaji.SingleBungle.cafe.model.vo.Cafe;
import com.gaji.SingleBungle.cafe.model.vo.CafeAttachment;
import com.gaji.SingleBungle.findFriend.model.service.FindFriendService;
import com.gaji.SingleBungle.findFriend.model.vo.FindFriend;
import com.gaji.SingleBungle.findFriend.model.vo.FindFriendAttachment;
import com.gaji.SingleBungle.market.model.service.MarketService;
import com.gaji.SingleBungle.market.model.vo.Market;
import com.gaji.SingleBungle.market.model.vo.MarketAttachment;
import com.gaji.SingleBungle.member.model.vo.MReply;
import com.gaji.SingleBungle.member.model.vo.Member;
import com.gaji.SingleBungle.review.model.service.ReviewService;
import com.gaji.SingleBungle.review.model.vo.Review;
import com.gaji.SingleBungle.review.model.vo.ReviewAttachment;
import com.gaji.SingleBungle.review.model.vo.ReviewLike;
import com.gaji.SingleBungle.review.model.vo.ReviewPageInfo;
import com.gaji.SingleBungle.review.model.vo.ReviewSearch;
import com.google.gson.Gson;

@Controller
@SessionAttributes({ "loginMember" })
@RequestMapping("/admin/*")
public class adminController {

	@Autowired
	private adminService service;
	private BoardService service1;
	private ReviewService service2;
	private CafeService service6;
	private FindFriendService service7;
	private MarketService service8;

	private String swalIcon = null;
	private String swalTitle = null;
	private String swalText = null;

	@RequestMapping("eventList")
	public String eventListView(@RequestParam(value = "cp", required = false, defaultValue = "1") int cp, Model model) {

		int type = 4;
		APageInfo pInfo = service.getPageInfo(cp, type);
		pInfo.setLimit(9);
		List<ABoard> eventList = service.selectList(pInfo, type);

		if (eventList != null && !eventList.isEmpty()) { // ??????????????? ????????? ????????? ???????????? ??????
			List<AAttachment> thumbnailList = service.selectThumbnailList(eventList);

			if (thumbnailList != null) {
				model.addAttribute("thList", thumbnailList);
			}

		}

		model.addAttribute("eventList", eventList);
		model.addAttribute("pInfo", pInfo);

		return "admin/eventList";
	}

	@RequestMapping("event/{boardNo}")
	public String eventView(@PathVariable("boardNo") int boardNo, Model model,
			@RequestHeader(value = "referer", required = false) String referer, RedirectAttributes ra) {

		int type = 4;
		ABoard board = service.selectBoard(boardNo, type);

		String url = null;

		if (board != null) { // ?????? ?????? ?????? ???

			// ?????? ?????? ????????? ???????????? ????????? ????????? ???????????? Service ??????
			List<AAttachment> attachmentList = service.selectAttachmentList(boardNo);

			if (attachmentList != null & !attachmentList.isEmpty()) {
				model.addAttribute("attachmentList", attachmentList);
			}

			model.addAttribute("board", board);
			url = "admin/eventView";
		} else {

			if (referer == null) {// ?????? ?????? ????????? ?????? ??????(ex. ???????????? , ??????????????? ?????? ????????? ?????? ???)
				url = "redirect:../";
			} else {// ?????? ?????? ????????? ?????? ??????(ex. ??????????????? ????????? ?????? ??????????????? ?????? ?????? ???)
				url = "redirect:" + referer;
			}
			ra.addFlashAttribute("swalIcon", "error");
			ra.addFlashAttribute("swalTitle", "???????????? ?????? ??????????????????.");
		}
		return url;
	}

	@RequestMapping("eventInsert")
	public String eventInsert() {
		return "admin/eventInsert";
	}

	// ????????? ??????Controller
	@RequestMapping("eventInsertAction")
	public String eventInsertAction(@ModelAttribute ABoard board, @ModelAttribute("loginMember") Member loginMember,
			@RequestParam(value = "images", required = false) List<MultipartFile> images, HttpServletRequest request,
			RedirectAttributes ra) {
		// @ModelAttribute Board board == categoryName,boardTitle, boardContent ?????????
		// @RequestParam(value="images", required=false) List<MultipartFile> images
		// -> <input type="file" name="images"> ????????? ?????? ????????? images?????? List??? ??????

		// map??? ???????????? ????????? ????????? ?????? ??????
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("boardTitle", board.getBoardTitle());
		map.put("boardContent", board.getBoardContent()); // name ??? categoryName ????????? value??? ????????? ????????????.

		String savePath = null;

		savePath = request.getSession().getServletContext().getRealPath("resources/adminImages");

		System.out.println(images);

		// ????????? map, ????????? images, ???????????? savePath
		// ????????? ?????? Service ??????
		int result = service.insertEvent(map, images, savePath);

		String url = null;

		// ????????? ?????? ????????? ?????? View ?????? ??????
		if (result > 0) {
			swalIcon = "success";
			swalTitle = "????????? ?????? ??????";
			url = "redirect:event/" + result;

			// ?????? ????????? ????????? ?????? ?????? ??? ???????????? ?????? ?????? ????????????
			request.getSession().setAttribute("returnListURL", "../");

		} else {
			swalIcon = "error";
			swalTitle = "????????? ?????? ??????";
			url = "redirect:noticeInsert";
		}

		ra.addFlashAttribute("swalIcon", swalIcon);
		ra.addFlashAttribute("swalTitle", swalTitle);

		return url;
	}

	// ----------------------------------------------------FAQ

	@RequestMapping("faqInsert")
	public String faqInsert() {
		return "admin/faqInsert";
	}

	// ????????? ??????Controller
	@RequestMapping("insertFaqAction")
	public String insertFaqAction(@ModelAttribute ABoard board, @RequestParam(value = "categoryCode") int categoryCode,
			HttpServletRequest request, RedirectAttributes ra) {

		System.out.println(categoryCode);
		System.out.println(board);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("boardTitle", board.getBoardTitle());
		map.put("boardContent", board.getBoardContent());
		map.put("categoryCode", categoryCode);

		// ????????? map, ????????? images, ???????????? savePath
		// ????????? ?????? Service ??????
		int result = service.insertFaqAction(map);

		String url = null;

		// ????????? ?????? ????????? ?????? View ?????? ??????
		if (result > 0) {
			swalIcon = "success";
			swalTitle = "????????? ?????? ??????";
			url = "redirect:faqView";

			// ?????? ????????? ????????? ?????? ?????? ??? ???????????? ?????? ?????? ????????????
			request.getSession().setAttribute("returnListURL", "../");

		} else {
			swalIcon = "error";
			swalTitle = "????????? ?????? ??????";
			url = "redirect:noticeInsert";
		}

		ra.addFlashAttribute("swalIcon", swalIcon);
		ra.addFlashAttribute("swalTitle", swalTitle);

		return url;
	}

	@RequestMapping("faqView")
	public String faqView(Model model) {

		int type = 5;
		List<ABoard> board = service.selectFaqList(type);

		model.addAttribute("board", board);

		return "admin/faqView";
	}
	
	
	
	
	// ????????? ??????
		@RequestMapping("search")
		public String searchBoard(@RequestParam(value="cp", required=false, defaultValue ="1")  int cp,
									@RequestParam(value="ct",required = false) String ct,
									Model model) {
			
			
			List<ABoard> board = service.selectSearchList(ct);
			
			
			model.addAttribute("board", board);
			model.addAttribute("ct",ct);
			
			
			return "admin/faqView";
		}

	// -------------------------------notice

	@RequestMapping("noticeInsert")
	public String noticeInsert() {
		return "admin/noticeInsert";
	}

	// ????????? ??????Controller
	@RequestMapping("noticeInsertAction")
	public String insertAction(@ModelAttribute ABoard board, @ModelAttribute("loginMember") Member loginMember,
			@RequestParam(value = "images", required = false) List<MultipartFile> images, HttpServletRequest request,
			RedirectAttributes ra) {
		// @ModelAttribute Board board == categoryName,boardTitle, boardContent ?????????
		// @RequestParam(value="images", required=false) List<MultipartFile> images
		// -> <input type="file" name="images"> ????????? ?????? ????????? images?????? List??? ??????

		// map??? ???????????? ????????? ????????? ?????? ??????
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("boardTitle", board.getBoardTitle());
		map.put("boardContent", board.getBoardContent()); // name ??? categoryName ????????? value??? ????????? ????????????.

		String savePath = null;

		savePath = request.getSession().getServletContext().getRealPath("resources/adminImages");

		System.out.println(images);

		// ????????? map, ????????? images, ???????????? savePath
		// ????????? ?????? Service ??????
		int result = service.insertNotice(map, images, savePath);

		String url = null;

		// ????????? ?????? ????????? ?????? View ?????? ??????
		if (result > 0) {
			swalIcon = "success";
			swalTitle = "????????? ?????? ??????";
			url = "redirect:notice/" + result;

			// ?????? ????????? ????????? ?????? ?????? ??? ???????????? ?????? ?????? ????????????
			request.getSession().setAttribute("returnListURL", "../");

		} else {
			swalIcon = "error";
			swalTitle = "????????? ?????? ??????";
			url = "redirect:noticeInsert";
		}

		ra.addFlashAttribute("swalIcon", swalIcon);
		ra.addFlashAttribute("swalTitle", swalTitle);

		return url;
	}

	@RequestMapping("noticeList")
	public String noticeListView(@RequestParam(value = "cp", required = false, defaultValue = "1") int cp,
			Model model) {

		int type = 3;
		APageInfo pInfo = service.getPageInfo(cp, type);
		pInfo.setLimit(10);
		List<ABoard> noticeList = service.selectList(pInfo, type);

		if (noticeList != null && !noticeList.isEmpty()) { // ??????????????? ????????? ????????? ???????????? ??????
			List<AAttachment> thumbnailList = service.selectThumbnailList(noticeList);

			if (thumbnailList != null) {
				model.addAttribute("thList", thumbnailList);
			}

		}

		model.addAttribute("noticeList", noticeList);
		model.addAttribute("pInfo", pInfo);

		return "admin/noticeList";
	}

	@RequestMapping("notice/{boardNo}")
	public String noticeView(@PathVariable("boardNo") int boardNo, Model model,
			@RequestHeader(value = "referer", required = false) String referer, RedirectAttributes ra) {

		int type = 3;
		ABoard board = service.selectBoard(boardNo, type);

		String url = null;

		if (board != null) { // ?????? ?????? ?????? ???

			// ?????? ?????? ????????? ???????????? ????????? ????????? ???????????? Service ??????
			List<AAttachment> attachmentList = service.selectAttachmentList(boardNo);

			if (attachmentList != null & !attachmentList.isEmpty()) {
				model.addAttribute("attachmentList", attachmentList);
			}

			model.addAttribute("board", board);
			url = "admin/noticeView";
		} else {

			if (referer == null) {// ?????? ?????? ????????? ?????? ??????(ex. ???????????? , ??????????????? ?????? ????????? ?????? ???)
				url = "redirect:../";
			} else {// ?????? ?????? ????????? ?????? ??????(ex. ??????????????? ????????? ?????? ??????????????? ?????? ?????? ???)
				url = "redirect:" + referer;
			}
			ra.addFlashAttribute("swalIcon", "error");
			ra.addFlashAttribute("swalTitle", "???????????? ?????? ??????????????????.");
		}
		return url;

	}

	// ------------------------------------------------delete
	@RequestMapping("{boardNo}/{boardCode}/delete")
	public String noticeDelete(@PathVariable("boardNo") int boardNo, @PathVariable("boardCode") int boardCode,
			@RequestHeader(value = "referer", required = false) String referer, RedirectAttributes ra) {

		// System.out.println(boardNo);

		System.out.println(boardCode);
		System.out.println(boardNo);

		/*
		 * int boardNo = Integer.parseInt(boardNo1); int boardCode =
		 * Integer.parseInt(boardCode1);
		 */

		int result = service.deleteBoard(boardNo);
		String url = null;

		if (result > 0) {
			ra.addFlashAttribute("swalIcon", "success");
			ra.addFlashAttribute("swalTitle", "???????????????????????????.");

			switch (boardCode) {
			case 3:
				url = "redirect:../../noticeList";
				break;

			case 4:
				url = "redirect:../../eventList";
				break;
			default:
				url = "redirect:" + referer;
				break;
			}
		} else {
			url = "redirect:" + referer;
			ra.addFlashAttribute("swalIcon", "error");
			ra.addFlashAttribute("swalTitle", "???????????? ?????? ??????????????????.");
		}

		return url;

	}

	// ----------------------------------------------------------------------------------update
	// ????????? ?????? ?????? ??????
	@RequestMapping("{boardNo}/{boardCode}/update")
	public String update(@PathVariable("boardNo") int boardNo, @PathVariable("boardCode") int boardCode, Model model) {

		ABoard board = service.selectBoard(boardNo, boardCode);

		model.addAttribute("board", board);

		String url = null;
		if (boardCode == 3)
			url = "admin/noticeUpdate";
		else if (boardCode == 4)
			url = "admin/eventUpdate";

		return url;
	}

	// ????????? ??????
	@RequestMapping("updateAction")
	public String updateAction(@ModelAttribute ABoard updateBoard, Model model, RedirectAttributes ra,
			HttpServletRequest request, @RequestParam(value = "boardNo") int boardNo,
			@RequestParam(value = "boardCode") int boardCode) {

		updateBoard.setBoardNo(boardNo);
		updateBoard.setBoardCode(boardCode);
//			System.out.println(updateBoard);

//			// ?????? ?????? ?????? ????????????
		String savePath = request.getSession().getServletContext().getRealPath("resources/adminImages");

		int result = service.updateBoard(updateBoard, savePath);

		String url = null;
		String boardType = null;

		if (boardCode == 3)
			boardType = "notice/";
		else if (boardCode == 4)
			boardType = "event/";

		if (result > 0) {
			swalIcon = "success";
			swalTitle = "????????? ?????? ??????";
			url = "redirect:../admin/" + boardType + boardNo;

		} else {
			swalIcon = "error";
			swalTitle = "????????? ?????? ??????";
			url = "redirect:" + request.getHeader("referer");
		}

		ra.addFlashAttribute("swalIcon", swalIcon);
		ra.addFlashAttribute("swalTitle", swalTitle);

//			return url;
		return url;
	}

	// ---------------------------------------------summernote??? ???????????? ????????? ??????
	// Controller
	@ResponseBody
	@RequestMapping("insertImage")
	public String insertImage(HttpServletRequest request, @RequestParam("uploadFile") MultipartFile uploadFile) {
		// ?????? ?????? ??????(????????? ????????? ????????????) ????????? ??? ?????? -> HttpServletRequest request
		// ???????????? ?????? ?????? == ??????????????? ????????? ?????????
		// ->@RequestParam("uploadFile") MultipartFile uploadFile (uploadFile->???????????????
		// ???????????? )

		// ????????? ??????(?????????)??? ????????? ?????? ?????? ????????????
		String savePath = request.getSession().getServletContext().getRealPath("resources/adminImages");

		AAttachment at = service.insertImage(uploadFile, savePath);
		// ?????? ?????????, ?????? ????????? ?????????????????? service ??????

		System.out.println(at);

		// java?????? js??? ?????? ????????? ??? JSON??? ????????????.
		// ?????????????????? ?????????????????? ?????? ?????? "{}"??? ????????????.
		// gson??? ?????????.
		return new Gson().toJson(at);
	}

	@ResponseBody
	@RequestMapping("{boardNo}/{boardCode}/insertImage")
	public String insertImage2(HttpServletRequest request, @RequestParam("uploadFile") MultipartFile uploadFile) {
		// ?????? ?????? ??????(????????? ????????? ????????????) ????????? ??? ?????? -> HttpServletRequest request
		// ???????????? ?????? ?????? == ??????????????? ????????? ?????????
		// ->@RequestParam("uploadFile") MultipartFile uploadFile (uploadFile->???????????????
		// ???????????? )

		// ????????? ??????(?????????)??? ????????? ?????? ?????? ????????????
		String savePath = request.getSession().getServletContext().getRealPath("resources/adminImages");

		AAttachment at = service.insertImage(uploadFile, savePath);
		// ?????? ?????????, ?????? ????????? ?????????????????? service ??????

		System.out.println(at);

		// java?????? js??? ?????? ????????? ??? JSON??? ????????????.
		// ?????????????????? ?????????????????? ?????? ?????? "{}"??? ????????????.
		// gson??? ?????????.
		return new Gson().toJson(at);
	}

	// ---------------------------------------------------inquiry
	@RequestMapping("inquiryInsert")
	public String inquiryInsert() {
		return "admin/inquiryInsert";
	}

	@RequestMapping("insertinquiryAction")
	public String insertinquiryAction(@ModelAttribute inquiry board,
			@RequestParam(value = "categoryCode") int categoryCode, @ModelAttribute("loginMember") Member loginMember,
			@RequestParam(value = "images", required = false) List<MultipartFile> images, HttpServletRequest request,
			RedirectAttributes ra) {

		System.out.println(categoryCode);
		System.out.println(board);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("inquiryTitle", board.getInquiryTitle());
		map.put("inquiryContent", board.getInquiryContent());
		map.put("categoryCode", categoryCode);
		map.put("memberNo", loginMember.getMemberNo());

		String savePath = null;

		savePath = request.getSession().getServletContext().getRealPath("resources/adminImages");

		// ????????? map, ????????? images, ???????????? savePath
		// ????????? ?????? Service ??????
		int result = service.insertinquiryAction(map, images, savePath);

		String url = null;

		// ????????? ?????? ????????? ?????? View ?????? ??????
		if (result > 0) {
			swalIcon = "success";
			swalTitle = "????????? ?????? ??????";
			url = "redirect:inquiryList";

			// ?????? ????????? ????????? ?????? ?????? ??? ???????????? ?????? ?????? ????????????
			request.getSession().setAttribute("returnListURL", "../");

		} else {
			swalIcon = "error";
			swalTitle = "????????? ?????? ??????";
			url = "redirect:inquiryInsert";
		}

		ra.addFlashAttribute("swalIcon", swalIcon);
		ra.addFlashAttribute("swalTitle", swalTitle);

		return url;
	}

	@RequestMapping("inquiryList")
	public String inquiryList(@RequestParam(value = "cp", required = false, defaultValue = "1") int cp, Model model,
			@ModelAttribute("loginMember") Member loginMember) {

		int memberNo = loginMember.getMemberNo();
		
		APageInfo pInfo = null;
		List<inquiry> inquiryList = null;
		
		if(memberNo != 4) {
			pInfo = service.getInquiryPageInfo(cp);
			pInfo.setLimit(10);
			inquiryList = service.inquiryList(pInfo, memberNo);
		}else {
			pInfo = service.getInquiryPageInfo(cp);
			pInfo.setLimit(10);
			inquiryList = service.inquiryAllList(pInfo);
		}

		model.addAttribute("inquiryList", inquiryList);
		model.addAttribute("pInfo", pInfo);
		return "admin/inquiryList";
	}

	@RequestMapping("inquiry/{inquiryNo}")
	public String inquiryView(@PathVariable("inquiryNo") int inquiryNo, Model model,
			@RequestHeader(value = "referer", required = false) String referer, RedirectAttributes ra) {

		inquiry inquiry = service.selectInquiryBoard(inquiryNo);

		System.out.println(inquiry);
		String url = null;

		if (inquiry != null) { // ?????? ?????? ?????? ???

			// ?????? ?????? ????????? ???????????? ????????? ????????? ???????????? Service ??????
			List<IAttachment> attachmentList = service.selectIAttachmentList(inquiryNo);

			if (attachmentList != null & !attachmentList.isEmpty()) {
				model.addAttribute("attachmentList", attachmentList);
			}

			model.addAttribute("inquiry", inquiry);
			url = "admin/inquiryView";
		} else {

			if (referer == null) {// ?????? ?????? ????????? ?????? ??????(ex. ???????????? , ??????????????? ?????? ????????? ?????? ???)
				url = "redirect:../";
			} else {// ?????? ?????? ????????? ?????? ??????(ex. ??????????????? ????????? ?????? ??????????????? ?????? ?????? ???)
				url = "redirect:" + referer;
			}
			ra.addFlashAttribute("swalIcon", "error");
			ra.addFlashAttribute("swalTitle", "???????????? ?????? ??????????????????.");
		}
		return url;
	}

	@RequestMapping("{inquiryNo}/inquiryDelete")
	public String inquiryDelete(@PathVariable("inquiryNo") int inquiryNo,
			@RequestHeader(value = "referer", required = false) String referer, RedirectAttributes ra) {

		int result = service.deleteInquiry(inquiryNo);
		String url = null;

		if (result > 0) {
			ra.addFlashAttribute("swalIcon", "success");
			ra.addFlashAttribute("swalTitle", "???????????????????????????.");
			url = "redirect:../inquiryList";

		} else {
			url = "redirect:" + referer;
			ra.addFlashAttribute("swalIcon", "error");
			ra.addFlashAttribute("swalTitle", "???????????? ?????? ??????????????????.");
		}

		return url;

	}
	
	
	
	
	
	

	// ------------------------------------------------------------------------
	// ????????? ????????????
	@RequestMapping("adminMypage")
	public String adminMypage(@RequestParam(value = "cp", required = false, defaultValue = "1") int cp, @RequestParam(value = "cp2", required = false, defaultValue = "1") int cp2, Model model) {

		APageInfo pInfo = service.getABoarPageInfo(cp);
		pInfo.setLimit(5);
		List<ABoard> boardList = service.selectABoardList(pInfo);
		model.addAttribute("boardList", boardList);
		model.addAttribute("pInfo", pInfo);
		
		
		APageInfo pInfo2 = service.getAReplyPageInfo(cp2);
		pInfo2.setLimit(10);
		List<MReply> replyList = service.selectAReplyList(pInfo2);
		model.addAttribute("replyList", replyList);
		model.addAttribute("pInfo2", pInfo2);

		return "admin/adminMypage";
	}

	// -------------------------------------????????? ????????? ??????
	@RequestMapping("boardManage")
	public String boardManageView(@RequestParam(value = "cp", required = false, defaultValue = "1") int cp,
			Model model) {

		APageInfo pInfo = service.getAllPageInfo(cp);
		pInfo.setLimit(10);
		List<ABoard> boardList = service.selectAllList(pInfo);

		model.addAttribute("boardList", boardList);
		model.addAttribute("pInfo", pInfo);

		return "admin/boardManage";
	}
	
	
	// ????????? ??????
			@RequestMapping("boardSearch")
			public String boardSearch(@RequestParam(value="cp", required=false, defaultValue ="1")  int cp,
										@RequestParam(value="ct",required = false) String ct,
										Model model) {
				
				APageInfo pInfo = service.getSearchPageInfo(ct,cp);
				pInfo.setLimit(10);
				List<ABoard> boardList = service.selectBoardSearchList(ct, pInfo);
				
				
				model.addAttribute("boardList", boardList);
				model.addAttribute("pInfo",pInfo);
				model.addAttribute("ct",ct);
				
				
				return "admin/boardManage";
			}

	// ??????????????? ??????
	@ResponseBody
	@RequestMapping("recoverBoard")
	public boolean recoverBoard(@RequestParam(value = "boardNoList[]") int[] boardNoList,
			@RequestParam(value = "boardCodeList[]") int[] boardCodeList, @RequestHeader(value = "referer", required = false) String referer, RedirectAttributes ra) {
		int result = 0;
		boolean flag = false;
		
		for (int i = 0; i < boardNoList.length; i++) {
			int boardNo = boardNoList[i];
			int boardCode = boardCodeList[i];

			result = service.recoverBoard(boardNo, boardCode);

			if(result>0) {
				flag = true;
			}else {
				flag = false;
			}
		}

		return flag;
	}

	@RequestMapping("levelList")
	public String levelListView(@RequestParam(value = "cp", required = false, defaultValue = "1") int cp, Model model) {

		
		
		APageInfo pInfo = service.getGradePageInfo(cp);
		pInfo.setLimit(10);
		
		List<Member> memberList = service.selectGradeMember(pInfo);
		model.addAttribute("memberList", memberList);
		model.addAttribute("pInfo", pInfo);

		return "admin/levelList";
	}

	
	// ????????? ??????
	@RequestMapping("memberGradeSearch")
	public String memberGradeSearch(@RequestParam(value="cp", required=false, defaultValue ="1")  int cp,
								@RequestParam(value="ct",required = false) String ct,
								Model model) {
		
		APageInfo pInfo = service.getSearchmemberGradePageInfo(ct,cp);
		pInfo.setLimit(10);
		List<ABoard> memberList = service.selectSearchmemberGradeList(ct, pInfo);
		
		
		model.addAttribute("memberList", memberList);
		model.addAttribute("pInfo",pInfo);
		model.addAttribute("ct",ct);
		
		
		return "admin/levelList";
	}
	
	
	
	@ResponseBody
	@RequestMapping("gradeMember")
	public boolean gradeMember(@RequestParam(value = "memberNoList[]") int[] memberNoList, @RequestParam(value = "gradeList[]") String[] gradeList, @RequestHeader(value = "referer", required = false) String referer, RedirectAttributes ra) {
		int result = 0;
		boolean flag = false;
		
		for (int i = 0; i < memberNoList.length; i++) {
			int memNo = memberNoList[i];
			String grade = gradeList[i];

			result = service.gradeMember(memNo,grade);

			if(result>0) {
				flag = true;
			}else {
				flag = false;
			}
		}

		return flag;
	}	
	
	
	
	@RequestMapping("memberList")
	public String memberListView(@RequestParam(value = "cp", required = false, defaultValue = "1") int cp, Model model) {

		APageInfo pInfo = service.getMemberPageInfo(cp);
		pInfo.setLimit(10);
		
		List<Member> memberList = service.selectAllMember(pInfo);
		model.addAttribute("memberList", memberList);
		model.addAttribute("pInfo", pInfo);
		
		return "admin/memberList";
	}
	
	
	// ????????? ??????
	@RequestMapping("memberSearch")
	public String memberSearch(@RequestParam(value="cp", required=false, defaultValue ="1")  int cp,
								@RequestParam(value="ct",required = false) String ct,
								Model model) {
		
		APageInfo pInfo = service.getSearchmemberPageInfo(ct,cp);
		pInfo.setLimit(10);
		List<ABoard> memberList = service.selectSearchmemberList(ct, pInfo);
		
		
		model.addAttribute("memberList", memberList);
		model.addAttribute("pInfo",pInfo);
		model.addAttribute("ct",ct);
		
		
		return "admin/memberList";
	}
	
	
	@ResponseBody
	@RequestMapping("deleteMember")
	public boolean deleteMember(@RequestParam(value = "memberNoList[]") int[] memberNoList, @RequestHeader(value = "referer", required = false) String referer, RedirectAttributes ra) {
		int result = 0;
		boolean flag = false;
		
		for (int i = 0; i < memberNoList.length; i++) {
			int memNo = memberNoList[i];

			result = service.deleteMember(memNo);

			if(result>0) {
				flag = true;
			}else {
				flag = false;
			}
		}

		return flag;
	}
	
	
	@ResponseBody
	@RequestMapping("recoverMember")
	public boolean recoverMember(@RequestParam(value = "memberNoList[]") int[] memberNoList, @RequestHeader(value = "referer", required = false) String referer, RedirectAttributes ra) {
		int result = 0;
		boolean flag = false;
		
		for (int i = 0; i < memberNoList.length; i++) {
			int memNo = memberNoList[i];

			result = service.recoverMember(memNo);

			if(result>0) {
				flag = true;
			}else {
				flag = false;
			}
		}

		return flag;
	}

	
	
	@RequestMapping("replyManage")
	public String replyManageView(@RequestParam(value = "cp", required = false, defaultValue = "1") int cp, Model model) {

		APageInfo pInfo = service.getReplyPageInfo(cp);
		pInfo.setLimit(10);
		
		List<Reply> replyList = service.selectAllReply(pInfo);
		model.addAttribute("replyList", replyList);
		model.addAttribute("pInfo", pInfo);
		return "admin/replyManage";
	}
	
	
	// ????????? ??????
	@RequestMapping("replySearch")
	public String replySearch(@RequestParam(value = "cp", required = false, defaultValue = "1") int cp,
			@RequestParam(value = "ct", required = false) String ct, Model model) {

		APageInfo pInfo = service.getSearchReplyPageInfo(ct, cp);
		pInfo.setLimit(10);
		List<ABoard> replyList = service.selectSearchReplyList(ct, pInfo);

		model.addAttribute("replyList", replyList);
		model.addAttribute("pInfo", pInfo);
		model.addAttribute("ct", ct);

		return "admin/replyManage";
	}
	
	
	@ResponseBody
	@RequestMapping("recoverReply")
	public boolean recoverReply(@RequestParam(value = "replyNoList[]") int[] replyNoList, @RequestParam(value = "boardCodeList[]") int[] boardCodeList, @RequestHeader(value = "referer", required = false) String referer, RedirectAttributes ra) {
		int result = 0;
		boolean flag = false;
		
		for (int i = 0; i < replyNoList.length; i++) {
			int replyNo = replyNoList[i];
			int boardCode = boardCodeList[i];

			result = service.recoverReply(replyNo,boardCode);

			if(result>0) {
				flag = true;
			}else {
				flag = false;
			}
		}

		return flag;
	}
	
	
	@RequestMapping("boardReport")
	public String boardReportView(@RequestParam(value = "cp", required = false, defaultValue = "1") int cp, Model model) {

		APageInfo pInfo = service.getReportBoardPageInfo(cp);
		pInfo.setLimit(10);
		
		List<reportBoard> boardList = service.selectReportBoard(pInfo);
		model.addAttribute("boardList", boardList);
		model.addAttribute("pInfo", pInfo);
		
		return "admin/boardReport";
	}
	
	
	// ????????? ??????
	@RequestMapping("reportBoardSearch")
	public String reportBoardSearch(@RequestParam(value="cp", required=false, defaultValue ="1")  int cp,
								@RequestParam(value="ct",required = false) String ct,
								Model model) {
		
		APageInfo pInfo = service.getSearchRBoardPageInfo(ct,cp);
		pInfo.setLimit(10);
		List<ABoard> boardList = service.selectSearchRBoardList(ct, pInfo);
		
		
		model.addAttribute("boardList", boardList);
		model.addAttribute("pInfo",pInfo);
		model.addAttribute("ct",ct);
		
		
		return "admin/boardReport";
	}

	
	@ResponseBody
	@RequestMapping("recoverReportBoard")
	public boolean recoverReportBoard(@RequestParam(value = "reportNoList[]") int[] reportNoList, @RequestParam(value = "boardCodeList[]") int[] boardCodeList, @RequestHeader(value = "referer", required = false) String referer, RedirectAttributes ra) {
		int result = 0;
		boolean flag = false;
		
		for (int i = 0; i < reportNoList.length; i++) {
			int reportNo = reportNoList[i];
			int boardCode = boardCodeList[i];

			Map<String, Integer> map = new HashMap<String, Integer>();
			map.put("reportNo", reportNo);
			map.put("boardCode", boardCode);
			
			result = service.recoverReportBoard(map);

			if(result>0) {
				flag = true;
			}else {
				flag = false;
			}
		}

		return flag;
	}
	
	
	@ResponseBody
	@RequestMapping("deleteReportBoard")
	public boolean deleteReportBoard(@RequestParam(value = "reportNoList[]") int[] reportNoList, @RequestParam(value = "boardNoList[]") int[] boardNoList, @RequestParam(value = "boardCodeList[]") int[] boardCodeList, @RequestHeader(value = "referer", required = false) String referer, RedirectAttributes ra) {
		int result = 0;
		boolean flag = false;
		
		for (int i = 0; i < boardNoList.length; i++) {
			int boardNo = boardNoList[i];
			int boardCode = boardCodeList[i];
			int reportNo = reportNoList[i];
			
			Map<String, Integer> map = new HashMap<String, Integer>();
			map.put("boardNo", boardNo);
			map.put("boardCode", boardCode);
			
			Map<String, Integer> map2 = new HashMap<String, Integer>();
			map2.put("reportNo", reportNo);
			map2.put("boardCode", boardCode);
			
			result = service.deleteReportBoard(map);
			
			if(result>0) {
				result = service.recoverReportBoard(map2);
				if(result>0) {
					flag = true;
				}else {
					flag = false;
					System.out.println("?????? ?????? ??????");
				}	
			}else {
				flag = false;
				System.out.println("?????? ??????????????? ??????");
			}
		}
		return flag;
	}
	
	
	
	@RequestMapping("replyReport")
	public String replyReportView(@RequestParam(value = "cp", required = false, defaultValue = "1") int cp, Model model) {

		APageInfo pInfo = service.getReportReplyPageInfo(cp);
		pInfo.setLimit(10);
		
		List<reportReply> replyList = service.selectReportReply(pInfo);
		model.addAttribute("replyList", replyList);
		model.addAttribute("pInfo", pInfo);
		
		System.out.println(replyList);
		
		return "admin/replyReport";
	}
	
	
	
	// ????????? ??????
		@RequestMapping("reportReplySearch")
		public String reportReplySearch(@RequestParam(value="cp", required=false, defaultValue ="1")  int cp,
									@RequestParam(value="ct",required = false) String ct,
									Model model) {
			
			APageInfo pInfo = service.getSearchRReplyPageInfo(ct,cp);
			pInfo.setLimit(10);
			List<ABoard> replyList = service.selectSearchRReplyList(ct, pInfo);
			
			
			model.addAttribute("replyList", replyList);
			model.addAttribute("pInfo",pInfo);
			model.addAttribute("ct",ct);
			
			
			return "admin/replyReport";
		}
		
		
	
	@ResponseBody
	@RequestMapping("recoverReportReply")
	public boolean recoverReportReply(@RequestParam(value = "reportNoList[]") int[] reportNoList, @RequestParam(value = "boardCodeList[]") int[] boardCodeList, @RequestHeader(value = "referer", required = false) String referer, RedirectAttributes ra) {
		int result = 0;
		boolean flag = false;
		
		for (int i = 0; i < reportNoList.length; i++) {
			int reportNo = reportNoList[i];
			int boardCode = boardCodeList[i];

			System.out.println("boardCode" + boardCode);
			Map<String, Integer> map = new HashMap<String, Integer>();
			map.put("reportNo", reportNo);
			map.put("boardCode", boardCode);
			
			result = service.recoverReportReply(map);

			if(result>0) {
				flag = true;
			}else {
				flag = false;
			}
		}

		return flag;
	}
	
	
	@ResponseBody
	@RequestMapping("deleteReportReply")
	public boolean deleteReportReply(@RequestParam(value = "replyNoList[]") int[] replyNoList, @RequestParam(value = "reportNoList[]") int[] reportNoList, @RequestParam(value = "boardCodeList[]") int[] boardCodeList, @RequestHeader(value = "referer", required = false) String referer, RedirectAttributes ra) {
		int result = 0;
		boolean flag = false;
		
		for (int i = 0; i < replyNoList.length; i++) {
			int replyNo = replyNoList[i];
			int boardCode = boardCodeList[i];
			int reportNo = reportNoList[i];
			
			Map<String, Integer> map = new HashMap<String, Integer>();
			map.put("replyNo", replyNo);
			map.put("boardCode", boardCode);
			
			Map<String, Integer> map2 = new HashMap<String, Integer>();
			map2.put("reportNo", reportNo);
			map2.put("boardCode", boardCode);
			
			result = service.deleteReportReply(map);
			
			System.out.println(" boardCode: " + boardCode);
			System.out.println(" replyNo: " + replyNo);
			System.out.println(" reportNo: " + reportNo);
			
			
			if(result>0) {
				result = service.recoverReportReply(map2);
				if(result>0) {
					flag = true;
				}else {
					flag = false;
					System.out.println("?????? ?????? ??????");
				}	
			}else {
				flag = false;
				System.out.println("?????? ??????????????? ??????");
			}
		}
		return flag;
	}

}
