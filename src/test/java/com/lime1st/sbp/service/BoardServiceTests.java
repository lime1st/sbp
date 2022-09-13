package com.lime1st.sbp.service;

import com.lime1st.sbp.dto.BoardDTO;
import com.lime1st.sbp.dto.PageRequestDTO;
import com.lime1st.sbp.dto.PageResultDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister(){

        //  email 추가해야 함
        String email = "";

        BoardDTO dto = BoardDTO.builder()
                .title("Test..")
                .content("Test...")
                .writerEmail(email)
                .build();

        Long bno = boardService.register(dto);
    }

    @Test
    public void testList(){

        PageRequestDTO pageRequestDTO = new PageRequestDTO();

        PageResultDTO<BoardDTO, Object[]> result = boardService.getList(pageRequestDTO);

        for(BoardDTO boardDTO : result.getDtoList()){
            System.out.println(boardDTO);
        }
    }

    @Test
    public void testGet(){

        Long bno = 100L;

        BoardDTO boardDTO = boardService.get(bno);

        System.out.println(boardDTO);
    }

    @Test
    public void testRemove(){

        Long bno = 33L;

        boardService.removeWithReplies(bno);
    }

    @Test
    public void testModify(){

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(2L)
                .title("제목 변경 테스트")
                .content("내용 변경 테스트")
                .build();

        boardService.modify(boardDTO);
    }
}
