package memstore

import (
	"context"
	"fmt"
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestMemStore(t *testing.T) {

	var (
		ctx   = context.Background()
		store = NewMemStore(5)
	)

	vs, err := store.Get(ctx, "key", 1)
	assert.Nil(t, err)
	assert.Nil(t, vs)

	for i := 1; i <= 10; i++ {
		assert.Nil(t, store.Put(ctx, "key", fmt.Sprintf("%d", i)))
	}

	vs, err = store.Get(ctx, "key", 2)
	assert.Nil(t, err)
	assert.Equal(t, 2, len(vs))
	assert.Equal(t, []string{"10", "9"}, vs)

	vs, err = store.Get(ctx, "key", 10)
	assert.Nil(t, err)
	assert.Equal(t, 5, len(vs))
}
