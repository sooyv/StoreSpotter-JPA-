package com.sojoo.StoreSpotter.controller.admin;

import com.sojoo.StoreSpotter.api.ApiResult;
import com.sojoo.StoreSpotter.api.MgrSwaggerDoc;
import com.sojoo.StoreSpotter.common.error.ErrorCode;
import com.sojoo.StoreSpotter.common.exception.ApiDataNotFoundException;
import com.sojoo.StoreSpotter.service.apiToDb.StoreInfoService;
import com.sojoo.StoreSpotter.service.storePair.DataPairService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final DataPairService dataPairService;
    private final StoreInfoService storeInfoService;

    @GetMapping
    @Operation(summary = MgrSwaggerDoc.Admin.Api.page.ADMIN_SUMMARY, description = MgrSwaggerDoc.Admin.Api.page.ADMIN_DESC)
    @ApiResponse(responseCode = MgrSwaggerDoc.Response.Success.Code, description = MgrSwaggerDoc.Response.Success.Desc)
    public ModelAndView index() {
        return new ModelAndView("admin/admin");
    }


    @Operation(summary = MgrSwaggerDoc.Admin.Api.dataPair.Summary, description = MgrSwaggerDoc.Admin.Api.dataPair.Desc)
    @PostMapping("/dataPair")
    @ApiResult(
            successCode = MgrSwaggerDoc.Response.Success.Code,
            successDescription = MgrSwaggerDoc.Response.Success.Desc,
            successSchema = Void.class, errors = { ErrorCode.DATA_PAIR_CREATE_FAILED })
    public void dataPairs() {
        dataPairService.saveIndustryPairData();
    }


    @Operation(summary = MgrSwaggerDoc.Admin.Api.dataPair.Summary, description = MgrSwaggerDoc.Admin.Api.dataPair.Desc)
    @PostMapping("/conv-dataPair")
    @ApiResult(
            successCode = MgrSwaggerDoc.Response.Success.Code,
            successDescription = MgrSwaggerDoc.Response.Success.Desc,
            successSchema = Void.class, errors = { ErrorCode.DATA_PAIR_CREATE_FAILED })
    public void convDataPairs() {
        dataPairService.saveConvIndustryPairData();
    }


    @Operation(summary = MgrSwaggerDoc.Admin.Api.dataPair.Summary, description = MgrSwaggerDoc.Admin.Api.dataPair.Desc)
    @PostMapping("/cafe-dataPair")
    @ApiResult(
            successCode = MgrSwaggerDoc.Response.Success.Code,
            successDescription = MgrSwaggerDoc.Response.Success.Desc,
            successSchema = Void.class, errors = { ErrorCode.DATA_PAIR_CREATE_FAILED })
    public void cafeDataPairs() {
        dataPairService.saveCafeIndustryPairData();
    }


    @Operation(summary = MgrSwaggerDoc.Admin.Api.industriesApi.Summary , description = MgrSwaggerDoc.Admin.Api.industriesApi.Desc)
    @PostMapping("/apiDataSave")
    @ApiResult(
            successCode = MgrSwaggerDoc.Response.Success.Code,
            successDescription = MgrSwaggerDoc.Response.Success.Desc,
            successSchema = Void.class, errors = { ErrorCode.API_DATA_NOT_FOUND })
    public ResponseEntity<String> industries() {
        try {
            storeInfoService.apiToDb();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ApiDataNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @Operation(summary = MgrSwaggerDoc.Admin.Api.convApi.Summary, description = MgrSwaggerDoc.Admin.Api.convApi.Desc)
    @PostMapping("/conv-api")
    @ApiResult(
            successCode = MgrSwaggerDoc.Response.Success.Code,
            successDescription = MgrSwaggerDoc.Response.Success.Desc,
            successSchema = Void.class, errors = { ErrorCode.API_DATA_NOT_FOUND })
    public ResponseEntity<String> convApiSave() {
        try {
            storeInfoService.convApiToDb();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(ApiDataNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @Operation(summary = MgrSwaggerDoc.Admin.Api.cafeApi.Summary, description = MgrSwaggerDoc.Admin.Api.cafeApi.Desc)
    @PostMapping("/cafe-api")
    @ApiResult(
            successCode = MgrSwaggerDoc.Response.Success.Code,
            successDescription = MgrSwaggerDoc.Response.Success.Desc,
            successSchema = Void.class, errors = { ErrorCode.API_DATA_NOT_FOUND })
    public ResponseEntity<String> cafeApiSave() {
        try {
            storeInfoService.cafeApiToDb();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(ApiDataNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
