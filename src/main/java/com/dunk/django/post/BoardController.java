//package com.dunk.django.post;
//
//import com.dunk.django.domain.Board;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//
//@Controller
//@RequiredArgsConstructor
//@Log4j2
//@RequestMapping("/board")
//public abstract class BoardController {
//    private final BoardService service;
//
//    @GetMapping("/list")
//    public void list(@ModelAttribute("pageDTO")PageDTO pageDTO,Model model) {
//
//        GenericListDTO<BoardDTO, Board> result =
//        service.getList(pageDTO);
//
//        result.getDtoList().forEach(dto -> log.info(dto));
//
//        model.addAttribute("result", result);
//    }
//
//    @GetMapping("/register")
//    public void register(@ModelAttribute("pageDTO")PageDTO pageDTO) {
//        log.info("/register.....................");
//    }
//
//    @PostMapping("/register")
//    public String register(BoardDTO dto, RedirectAttributes rttr){
//        log.info("POST Register.....................");
//        log.info("dto : " + dto);
//
//        Long bno = service.register(dto);
//
//        rttr.addFlashAttribute("result", bno);
//
//        return "redirect:/board/list";
//    }
//
//    @GetMapping({"/get", "/modify"})
//    public void get(Long bno, @ModelAttribute("pageDTO")PageDTO pageDTO, Model model) {
//        log.info("/get.................");
//        log.info("bno : " + bno);
//        log.info("pageDTO : " + pageDTO);
//
//        model.addAttribute("get", service.get(bno));
//
//    }
//
//    @PostMapping("/modify")
//    public String modify(BoardDTO dto, @ModelAttribute("pageDTO")PageDTO pageDTO, RedirectAttributes rttr) {
//        log.info("POST MODIFY------------------------------------------");
//        log.info(dto);
//
//        service.modify(dto);
//        rttr.addAttribute("bno", dto.getBno());
//        rttr.addAttribute("page", pageDTO.getPage());
//        rttr.addAttribute("size", pageDTO.getSize());
//
//        return "redirect:/board/get";
//    }
//
//    @PostMapping("/remove")
//    public String remove(Long bno) {
//        log.info("/remove...........................");
//        log.info("bno : " + bno);
//        service.remove(bno);
//
//        return "redirect:/board/list";
//    }
//
//}