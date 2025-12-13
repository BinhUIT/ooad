package com.example.ooad.service.sysparam.implementation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import com.example.ooad.domain.entity.SysParam;
import com.example.ooad.domain.entity.SysParamGroup;
import com.example.ooad.repository.SysParamGroupRepository;
import com.example.ooad.repository.SysParamRepository;
import com.example.ooad.service.sysparam.TypedParamValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SysParamServiceImplementationTest {

    @Mock
    private SysParamRepository sysParamRepository;

    @Mock
    private SysParamGroupRepository sysParamGroupRepository;

    @InjectMocks
    private SysParamServiceImplementation service;

    private SysParamGroup group;

    @BeforeEach
    void setUp() {
        group = new SysParamGroup();
        group.setGroupId(2);
        group.setGroupCode("EXAM_CONFIG");
        group.setGroupName("Cấu hình khám bệnh");
    }

    @Test
    void getTypedParamValue_number_integer() {
        SysParam p = new SysParam();
        p.setParamId(1);
        p.setGroup(group);
        p.setParamCode("MAX_PATIENTS_PER_DAY");
        p.setParamName("Số bệnh nhân tối đa/ngày");
        p.setParamValue("40");
        p.setDataType("NUMBER");
        p.setEffectiveFrom(LocalDate.now());

        when(sysParamRepository.findByParamCode("MAX_PATIENTS_PER_DAY")).thenReturn(Optional.of(p));

        TypedParamValue tv = service.getTypedParamValueByCode("MAX_PATIENTS_PER_DAY");
        assertEquals("NUMBER", tv.getDataType());
        assertTrue(tv.getValue() instanceof Integer || tv.getValue() instanceof Double);
        if (tv.getValue() instanceof Integer) {
            assertEquals(40, ((Integer) tv.getValue()).intValue());
        } else {
            assertEquals(40.0, ((Double) tv.getValue()).doubleValue());
        }
    }

    @Test
    void getTypedParamValue_boolean() {
        SysParam p = new SysParam();
        p.setParamId(2);
        p.setGroup(group);
        p.setParamCode("ALLOW_WALK_IN");
        p.setParamName("Cho phép khám không hẹn trước");
        p.setParamValue("true");
        p.setDataType("BOOLEAN");

        when(sysParamRepository.findByParamCode("ALLOW_WALK_IN")).thenReturn(Optional.of(p));

        TypedParamValue tv = service.getTypedParamValueByCode("ALLOW_WALK_IN");
        assertEquals("BOOLEAN", tv.getDataType());
        assertTrue(tv.getValue() instanceof Boolean);
        assertEquals(true, tv.getValue());
    }

    @Test
    void getTypedParamValue_time_parsedAsStringOrLocalTime() {
        SysParam p = new SysParam();
        p.setParamId(3);
        p.setGroup(group);
        p.setParamCode("MORNING_SHIFT_START");
        p.setParamName("Giờ bắt đầu ca sáng");
        p.setParamValue("08:00");
        p.setDataType("TIME");

        when(sysParamRepository.findByParamCode("MORNING_SHIFT_START")).thenReturn(Optional.of(p));

        TypedParamValue tv = service.getTypedParamValueByCode("MORNING_SHIFT_START");
        assertEquals("TIME", tv.getDataType());
        assertNotNull(tv.getValue());
        // Implementation currently returns String or LocalTime depending on parsing;
        // accept String
        assertTrue(tv.getValue() instanceof String || tv.getValue() instanceof java.time.LocalTime);
    }

    @Test
    void getParamValueByCode_returnsString() {
        SysParam p = new SysParam();
        p.setParamId(4);
        p.setGroup(group);
        p.setParamCode("EXAM_FEE");
        p.setParamName("Tiền khám");
        p.setParamValue("100000");
        p.setDataType("NUMBER");

        when(sysParamRepository.findByParamCode("EXAM_FEE")).thenReturn(Optional.of(p));

        String v = service.getParamValueByCode("EXAM_FEE");
        assertEquals("100000", v);
    }

    @Test
    void getTypedParamValue_notFound_throwsNotFound() {
        when(sysParamRepository.findByParamCode("UNKNOWN_CODE")).thenReturn(Optional.empty());

        assertThrows(com.example.ooad.exception.NotFoundException.class,
                () -> service.getTypedParamValueByCode("UNKNOWN_CODE"));
    }

    @Test
    void getTypedParamValue_number_double() {
        SysParam p = new SysParam();
        p.setParamId(5);
        p.setGroup(group);
        p.setParamCode("DECIMAL_PARAM");
        p.setParamName("Decimal param");
        p.setParamValue("12.5");
        p.setDataType("NUMBER");

        when(sysParamRepository.findByParamCode("DECIMAL_PARAM")).thenReturn(Optional.of(p));

        TypedParamValue tv = service.getTypedParamValueByCode("DECIMAL_PARAM");
        assertEquals("NUMBER", tv.getDataType());
        assertTrue(tv.getValue() instanceof Double);
        assertEquals(12.5, ((Double) tv.getValue()).doubleValue());
    }

    @Test
    void getTypedParamValue_number_malformed_throwsNumberFormat() {
        SysParam p = new SysParam();
        p.setParamId(6);
        p.setGroup(group);
        p.setParamCode("BAD_NUMBER");
        p.setParamName("Bad number");
        p.setParamValue("abc");
        p.setDataType("NUMBER");

        when(sysParamRepository.findByParamCode("BAD_NUMBER")).thenReturn(Optional.of(p));

        assertThrows(NumberFormatException.class, () -> service.getTypedParamValueByCode("BAD_NUMBER"));
    }

    @Test
    void getTypedParamValue_time_malformed_throwsDateTimeParse() {
        SysParam p = new SysParam();
        p.setParamId(7);
        p.setGroup(group);
        p.setParamCode("BAD_TIME");
        p.setParamName("Bad time");
        p.setParamValue("25:00");
        p.setDataType("TIME");

        when(sysParamRepository.findByParamCode("BAD_TIME")).thenReturn(Optional.of(p));

        assertThrows(java.time.format.DateTimeParseException.class, () -> service.getTypedParamValueByCode("BAD_TIME"));
    }

    @Test
    void getTypedParamValue_boolean_uppercase_true() {
        SysParam p = new SysParam();
        p.setParamId(8);
        p.setGroup(group);
        p.setParamCode("ALLOW_WALK_IN_UPPER");
        p.setParamName("Allow walk-in uppercase");
        p.setParamValue("TRUE");
        p.setDataType("BOOLEAN");

        when(sysParamRepository.findByParamCode("ALLOW_WALK_IN_UPPER")).thenReturn(Optional.of(p));

        TypedParamValue tv = service.getTypedParamValueByCode("ALLOW_WALK_IN_UPPER");
        assertEquals("BOOLEAN", tv.getDataType());
        assertTrue(tv.getValue() instanceof Boolean);
        assertEquals(true, tv.getValue());
    }
}
