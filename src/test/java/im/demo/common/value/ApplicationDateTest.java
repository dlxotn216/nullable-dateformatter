package im.demo.common.value;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by taesu on 2018-08-07.
 */
@RunWith(SpringRunner.class)
@Slf4j
public class ApplicationDateTest {

    @Test
    public void 생성_테스트() {
        ApplicationDate applicationDate = new ApplicationDate("2018-08-07");

        assertThat(applicationDate.getDate()).isEqualTo("2018-08-07");
        assertThat(applicationDate.getIsDayUnknown()).isEqualTo(false);
        assertThat(applicationDate.getIsMonthUnknown()).isEqualTo(false);

        log.info("Created is " + applicationDate);
    }

    @Test
    public void 월_UK_생성_테스트() {
        ApplicationDate applicationDate = new ApplicationDate("2018-UK-07");

        assertThat(applicationDate.getDate()).isEqualTo("2018-01-07");
        assertThat(applicationDate.getIsDayUnknown()).isEqualTo(false);
        assertThat(applicationDate.getIsMonthUnknown()).isEqualTo(true);
        log.info("Created is " + applicationDate);
    }

    @Test
    public void 일_UK_생성_테스트() {
        ApplicationDate applicationDate = new ApplicationDate("2018-08-UK");

        assertThat(applicationDate.getDate()).isEqualTo("2018-08-01");
        assertThat(applicationDate.getIsDayUnknown()).isEqualTo(true);
        assertThat(applicationDate.getIsMonthUnknown()).isEqualTo(false);
        log.info("Created is " + applicationDate);
    }

    @Test
    public void 일_월_UK_생성_테스트(){
        ApplicationDate applicationDate = new ApplicationDate("2018-UK-UK");

        assertThat(applicationDate.getDate()).isEqualTo("2018-01-01");
        assertThat(applicationDate.getIsDayUnknown()).isEqualTo(true);
        assertThat(applicationDate.getIsMonthUnknown()).isEqualTo(true);
        log.info("Created is " + applicationDate);
    }

    @Test
    public void 월_UK_포맷_테스트() {
        ApplicationDate applicationDate = new ApplicationDate("2018-UK-07");

        String formattedDate1 = applicationDate.getFormattedDate(ApplicationDateFormat.SYS_DATE_01);
        String formattedDate2 = applicationDate.getFormattedDate(ApplicationDateFormat.SYS_DATE_02);

        assertThat(formattedDate1).isEqualTo("2018-UK-07");
        assertThat(formattedDate2).isEqualTo("UK, 07, 2018");

        log.info("formattedDate1 : " + formattedDate1);
        log.info("formattedDate2 : " + formattedDate2);
    }

    @Test
    public void 일_UK_포맷_테스트() {
        ApplicationDate applicationDate = new ApplicationDate("2018-08-UK");

        String formattedDate1 = applicationDate.getFormattedDate(ApplicationDateFormat.SYS_DATE_01);
        String formattedDate2 = applicationDate.getFormattedDate(ApplicationDateFormat.SYS_DATE_02);

        assertThat(formattedDate1).isEqualTo("2018-Aug-UK");
        assertThat(formattedDate2).isEqualTo("Aug, UK, 2018");

        log.info("formattedDate1 : " + formattedDate1);
        log.info("formattedDate2 : " + formattedDate2);
    }

    @Test
    public void 일_월_UK_포맷_테스트(){
        ApplicationDate applicationDate = new ApplicationDate("2018-UK-UK");

        String formattedDate1 = applicationDate.getFormattedDate(ApplicationDateFormat.SYS_DATE_01);
        String formattedDate2 = applicationDate.getFormattedDate(ApplicationDateFormat.SYS_DATE_02);

        assertThat(formattedDate1).isEqualTo("2018-UK-UK");
        assertThat(formattedDate2).isEqualTo("UK, UK, 2018");

        log.info("formattedDate1 : " + formattedDate1);
        log.info("formattedDate2 : " + formattedDate2);
    }
}