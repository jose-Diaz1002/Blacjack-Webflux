package org.itacademy.blacjackwebflux.dto;

import org.itacademy.blacjackwebflux.model.mysql.Move;

import java.math.BigDecimal;

public class MakePlayRequest {
    private Move.MoveType moveType;
    private BigDecimal bet;

    public Move.MoveType getMoveType() {
        return moveType;
    }

    public void setMoveType(Move.MoveType moveType) {
        this.moveType = moveType;
    }

    public BigDecimal getBet() {
        return bet;
    }

    public void setBet(BigDecimal bet) {
        this.bet = bet;
    }
}


