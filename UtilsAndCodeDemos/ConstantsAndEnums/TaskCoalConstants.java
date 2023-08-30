

//枚举嵌套范例
public interface TaskCoalConstants {
    enum Status{
        领取(0),预约(1),签到(2),入场(3),称皮(4),称毛(5),离场(6), 入磅房(7),出磅房(8),入煤仓(9),出煤仓(10)
        ,放空(11),作废(12),入场申请(13),进入场院(14);
        private int val;
        Status(int val){
            this.val = val;
        }

        public int getVal() {
            return val;
        }
    }
}
