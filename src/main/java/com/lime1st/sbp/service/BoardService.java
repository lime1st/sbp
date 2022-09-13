package com.lime1st.sbp.service;

import com.lime1st.sbp.dto.BoardDTO;
import com.lime1st.sbp.dto.PageRequestDTO;
import com.lime1st.sbp.dto.PageResultDTO;
import com.lime1st.sbp.entity.Board;
import com.lime1st.sbp.entity.Member;

public interface BoardService {

    Long register(BoardDTO dto);

    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO);

    BoardDTO get(Long bno);

    void removeWithReplies(Long bno);

    default Board dtoToEntity(BoardDTO dto){

        //  member의 mid를 임의로 넣었다(이전 챕터에서는 이메일을 fk로 사용했다).
        //  게시글등록 시 mid를 직접 넣을 수 없으므로 이름으로 mid를 조회하도록 수정해야한다.->email로 키를 변경
        Member member = Member.builder().email("aaa@aaa.com").build();

        Board board = Board.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(member)
                .build();

        return board;
    }

    default BoardDTO entityToDTO(Board board, Member member, Long replyCount){

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .replyCount(replyCount.intValue())
                .build();
        return boardDTO;
    }

    void modify(BoardDTO boardDTO);
}
