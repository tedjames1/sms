package logic

import (
	"context"
	"fmt"
	"smsserver/pkg/xstring"

	"smsserver/internal/svc"
	"smsserver/internal/types"
	"smsserver/pkg/xtime"

	"github.com/zeromicro/go-zero/core/logx"
)

type PostSmsLogic struct {
	logx.Logger
	ctx    context.Context
	svcCtx *svc.ServiceContext
}

func NewPostSmsLogic(ctx context.Context, svcCtx *svc.ServiceContext) *PostSmsLogic {
	return &PostSmsLogic{
		Logger: logx.WithContext(ctx),
		ctx:    ctx,
		svcCtx: svcCtx,
	}
}

func (l *PostSmsLogic) PostSms(req *types.PutSmsReq) (resp *types.PutSmsResp, err error) {

	l.Debugf("put sms phone:%v content:%v", req.Phone, req.Content)

	key := xstring.CutEnd(req.Phone, 4)

	return &types.PutSmsResp{}, l.svcCtx.Store.Put(l.ctx, key, fmt.Sprintf("%v - %v", xtime.CurFormatedTime(), req.Content))
}
