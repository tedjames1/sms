package handler

import (
	"net/http"

	"github.com/zeromicro/go-zero/rest/httpx"

	"smsserver/internal/logic"
	"smsserver/internal/svc"
	"smsserver/internal/types"
)

func postSmsHandler(svcCtx *svc.ServiceContext) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		var req types.PutSmsReq
		if err := httpx.Parse(r, &req); err != nil {
			httpx.ErrorCtx(r.Context(), w, err)
			return
		}

		l := logic.NewPostSmsLogic(r.Context(), svcCtx)
		resp, err := l.PostSms(&req)
		if err != nil {
			httpx.ErrorCtx(r.Context(), w, err)
		} else {
			httpx.OkJsonCtx(r.Context(), w, resp)
		}
	}
}
