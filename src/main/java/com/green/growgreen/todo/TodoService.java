package com.green.growgreen.todo;

import com.green.growgreen.todo.model.*;
import com.green.growgreen.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class TodoService {
    private final TodoMapper MAPPER;

    @Autowired
    public TodoService(TodoMapper mapper) {
        this.MAPPER = mapper;
    }

    public int postTodo(TodoInsDto dto) {
        TodoEntity entity = new TodoEntity();
        entity.setIplant(dto.getIplant());
        entity.setCtnt(dto.getCtnt());
        entity.setRepeatYn(dto.getRepeatYn());
        entity.setDeadlineDate(dto.getDeadlineDate());
        entity.setDeadlineTime(dto.getDeadlineTime());

        //Todo등록
        MAPPER.insTodo(entity);

        // repeatYn이 1이면 p_day테이블에 인서트
        if( dto.getRepeatYn() == 1 ) {
            TodoRepeatDayDto repeatDto = new TodoRepeatDayDto();

            repeatDto.setItodo(entity.getItodo());

            for(int i=0; i<dto.getRepeatDay().size(); i++) {
                int repeatDay = dto.getRepeatDay().get(i);
                repeatDto.setRepeatDay(repeatDay);
                MAPPER.insRepeatDay(repeatDto);
            }
            return entity.getItodo();
        }
        return entity.getItodo();
    }

    public List<TodoVo> getTodo() {
        return MAPPER.selTodo();
    }

    public List<TodoVo> getTodoByDay(String deadline) {
        TodoSelDto dto = new TodoSelDto();
        dto.setDeadlineDate(deadline);
        List<TodoVo> result = MAPPER.selTodoByDay(dto);

        int year = Integer.parseInt(deadline.substring(0, 4));
        int mon = Integer.parseInt(deadline.substring(5, 7));
        int day = Integer.parseInt(deadline.substring(8));

        YearMonth targetDay = YearMonth.of(year, mon);
        List<TodoRepeatVo> repeatInfoList = MAPPER.selTodoRepeat(targetDay.toString());
        List<TodoVo> repeatVoList = createRepeatTodo(targetDay, repeatInfoList);
        for(TodoVo vo : repeatVoList) {
            String dbDeadlineDate = vo.getDeadlineDate();
            //int dbDay = Integer.parseInt(dbDeadlineDate.substring(3));
            if(Integer.parseInt(vo.getDeadlineDate().substring(3)) == day) {
                result.add(vo);
            }
        }
        return result;
    }

    public List<TodoVo> getTodoAll(int year, int mon) {
        YearMonth today = null;
        if(year == 0 || mon == 0) {
            today = YearMonth.now();
        } else {
            today = YearMonth.of(year, mon);
        }
        LocalDate todayStart = today.atDay(1);
        LocalDate todayEnd = today.atEndOfMonth();

        LocalDate calStart = todayStart.plusDays(-todayStart.getDayOfWeek().getValue());
        LocalDate calEnd = todayEnd.plusDays((6-todayEnd.getDayOfWeek().getValue()));

        TodoSelListDto dto = TodoSelListDto.builder()
                .sDate(calStart.toString())
                .eDate(calEnd.toString())
                .build();
        List<TodoVo> list = MAPPER.selTodoAll(dto);
        List<TodoVo> result = new LinkedList();
        result.addAll(list);

        //반복 여부 select
        List<TodoRepeatVo> repeatInfoList = MAPPER.selTodoRepeat(today.toString());
        List<TodoVo> repeatVoList = createRepeatTodo(today, repeatInfoList);
        result.addAll(repeatVoList);
        return result;
    }

    private List<TodoVo> createRepeatTodo(YearMonth today, List<TodoRepeatVo> repeatList) {
        List<TodoVo> list = new LinkedList();

        int todayYear = today.getYear();
        int todayMon = today.getMonthValue();

        //1일의 요일을 찾는다.
        LocalDate todayStart = today.atDay(1);
        int oneDayWeekOfMon = todayStart.getDayOfWeek().getValue();

        //이번달의 마지막일
        LocalDate todayEnd = today.atEndOfMonth();
        int todayEndDay = todayEnd.getDayOfMonth();

        for(TodoRepeatVo vo : repeatList) {
            String repeatStr = vo.getRepeatDay();
            String[] repeatWeekList = repeatStr.split(",");
            LocalDate voDate = vo.getDeadlineDate();


            int voYear = voDate.getYear();
            int voMon = voDate.getMonthValue();
            int voDay = voDate.getDayOfMonth();

            log.info("voYear : {}", voYear);
            log.info("voMon : {}", voMon);
            log.info("voDay : {}", voDay);
            //int voYear = Integer.parseInt(voDate.substring(0, 4));
            //int voMon = Integer.parseInt(voDate.substring(5, 7));
            //int voDay = Integer.parseInt(voDate.substring(8));

            for(String repeat : repeatWeekList) {
                //가장 빠른 같은 요일의 날짜를 찾음.
                //해달 월의 해당 요일의 첫 일
                int dayOfWeek = repeat.equals("6") ? 0 : Integer.parseInt(repeat) + 1; //요일은 자바 형식으로 컨버팅
                int lowDay = 1;
                if(oneDayWeekOfMon != dayOfWeek) {
                    lowDay += 7 - (oneDayWeekOfMon - dayOfWeek);
                }
                if(todayYear == voYear && todayMon == voMon) { //같은 년월이면 dealineDate보다 더 큰 값의 lowDay를 만든다.
                    while(lowDay <= voDay) {
                        lowDay += 7;
                    }
                }

                while(lowDay <= todayEndDay) {
                    TodoVo todo = new TodoVo();
                    todo.setItodo(vo.getItodo());
                    todo.setCtnt(vo.getCtnt());
                    todo.setDeadlineDate(String.format("%02d-%02d", voMon, lowDay));
                    todo.setDeadlineTime(vo.getDeadlineTime());
                    todo.setNickNm(vo.getNickNm());
                    todo.setNm(vo.getNm());
                    todo.setFinishYn(vo.getFinishYn());
                    list.add(todo);
                    lowDay += 7;
                }
            }
        }

        return list;
    }

    public TodoAllDto getTodoDetail(int itodo) {
        TodoDetailVo todo = MAPPER.selTodoDetail(itodo);
        List<String> repeatDay = MAPPER.selTodoRepeatDay(itodo);

        return TodoAllDto.builder().todo(todo).repeatDay(repeatDay).build();
    }

    public int putTodo(TodoUpdDto dto) {
        //repeatYn이 0이나 1 모두 p_todo테이블의 todo데이터를 수정
        MAPPER.updTodo(dto); // p_todo테이블에서 todo 수정

        //repeatYn = 0 인 경우
        if( dto.getRepeatYn() == 0 ) {
            TodoDelDto delDto = new TodoDelDto();
            delDto.setItodo(dto.getItodo()); // repeatYn=1에서 0으로 바뀐 경우에는 p_day에 있는 반복 데이터를 지워야하니깐 필요한 작업
            MAPPER.delRepeatDay(delDto.getItodo());
        }

        //repeatYn = 1 인 경우를 if문으로 먼저 확인
        if( dto.getRepeatYn() == 1 ) {
            TodoRepeatDayDto repeatDto = new TodoRepeatDayDto();

            repeatDto.setItodo(dto.getItodo());

            //p_day테이블에 있는 기존 반복 데이터 삭제
            TodoDelDto delDto = new TodoDelDto();
            delDto.setItodo(dto.getItodo());
            MAPPER.delRepeatDay(delDto.getItodo());

            // for문으로 선택한 반복요일 만큼 p_day테이블에 insert
            for(int i=0; i<dto.getRepeatDay().size(); i++) {
                int repeatDay = dto.getRepeatDay().get(i);
                repeatDto.setRepeatDay(repeatDay);
                MAPPER.insRepeatDay(repeatDto);
            }
        }
        return 1;
    }

    public int putTodoFinish(int itodo) {
        return MAPPER.updTodoFinish(itodo);
    }

    public int deleteTodo(int itodo) {
        //p_day테이블에서 삭제
        MAPPER.delRepeatDay(itodo);
        //p_todo테이블에서 삭제
        return MAPPER.delTodo(itodo);
    }

    @Scheduled(cron = "0 0 0 ? * *")
    public void insUpdRepeatDay (){
        log.info("오늘 자 반복 인서트 됨");
        List<TodoSelRepeatDayVo> list = MAPPER.selRepeatTodo();
        int day = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getRepeatDay() == FileUtils.getDate()){
                day = FileUtils.getDate();
            }
        }
        MAPPER.insUpdRepeatDay(day);
    }
}
