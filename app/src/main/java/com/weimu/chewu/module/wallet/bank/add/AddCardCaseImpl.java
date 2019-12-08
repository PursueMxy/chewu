package com.weimu.chewu.module.wallet.bank.add;

import android.text.TextUtils;

import com.weimu.chewu.backend.bean.base.NormalResponseB;
import com.weimu.chewu.backend.http.RetrofitClient;
import com.weimu.chewu.backend.http.rx.RxSchedulers;
import com.weimu.chewu.backend.remote.AddCardCase;
import com.weimu.universalib.utils.AuthUtils;
import com.weimu.universalib.utils.StringUtils;

import io.reactivex.Observable;

/**
 * Created by huangjinfu on 18/5/1.
 */

public class AddCardCaseImpl implements AddCardCase {

    @Override
    public Observable<NormalResponseB<String>> addBankCard(String cardholder, String card_no, String bank) {
        //此处能值的检查
        String checkResult = checkAllValue(cardholder, card_no, bank);
        if (!TextUtils.isEmpty(checkResult)) {
            //利用Rx返回错误
            return Observable.error(new IllegalArgumentException(checkResult));
        }
        return RetrofitClient.getDefault()
                .create(AddCardCase.class)
                .addBankCard(cardholder, card_no, bank)
                .compose(RxSchedulers.<NormalResponseB<String>>toMain());
    }

    @Override
    public Observable<NormalResponseB<String>> updateBankCard(String id, String cardholder, String card_no, String bank) {
        //此处能值的检查
        String checkResult = checkAllValue(cardholder, card_no, bank);
        if (!TextUtils.isEmpty(checkResult)) {
            //利用Rx返回错误
            return Observable.error(new IllegalArgumentException(checkResult));
        }
        return RetrofitClient.getDefault()
                .create(AddCardCase.class)
                .updateBankCard(id,cardholder, card_no, bank)
                .compose(RxSchedulers.<NormalResponseB<String>>toMain());
    }

    private String checkAllValue(String cardholder, String card_no, String bank) {

        if (TextUtils.isEmpty(cardholder)) {
            return "请输入持卡人姓名";
        }
        if (TextUtils.isEmpty(card_no)) {
            return "请输入银行卡号";
        }
        if (TextUtils.isEmpty(bank)) {
            return "请输入所在银行";
        }
        return null;
    }
}
