package logic

import (
	"context"
	"smsserver/pkg/xstring"

	"smsserver/internal/svc"
	"smsserver/internal/types"

	"github.com/zeromicro/go-zero/core/logx"
)

type GetSmsLogic struct {
	logx.Logger
	ctx    context.Context
	svcCtx *svc.ServiceContext
}

func NewGetSmsLogic(ctx context.Context, svcCtx *svc.ServiceContext) *GetSmsLogic {
	return &GetSmsLogic{
		Logger: logx.WithContext(ctx),
		ctx:    ctx,
		svcCtx: svcCtx,
	}
}

func (l *GetSmsLogic) GetSms(req *types.GetSmsReq) (resp *types.GetSmsResp, err error) {

	key := xstring.CutEnd(req.Phone, 4)

	contents, err := l.svcCtx.Store.Get(l.ctx, key, req.Count)
	if err != nil {
		l.Errorf("store err:%v", err)
	}

	l.Debugf("get sms req:%+v contents:%v", req, contents)

	return &types.GetSmsResp{Contents: contents}, err
}
