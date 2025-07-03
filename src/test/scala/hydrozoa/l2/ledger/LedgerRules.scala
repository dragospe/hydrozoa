package hydrozoa.l2.ledger


import org.scalacheck.Arbitrary
import scalus.cardano.ledger.rules.*
import scalus.cardano.ledger.ArbitraryInstances.given
import scalus.cardano.ledger.ArbitraryInstances
import scalus.cardano.ledger.Transaction

// A validator object that covers all the L2 ledger rules
object L2Validator extends STS.Validator {
    override def validate(context: Context, state: State, event: Event): Result = {
        for

            // Currently implemented in scalus, as of 2025-07-02
              _ <- NativeScriptsValidator.validate(context, state, event)
              _ <- MissingScriptsValidator.validate(context, state, event)
              _ <- VerifiedWitnessesValidator.validate(context, state, event)
              _ <- InputsAndReferenceInputsDisjointValidator.validate(context, state, event)
              _ <- AllInputsMustBeInUtxoValidator.validate(context, state, event)
        // Currently unimplemented in scalus, as of 2025-07-02 (
        // these names are from cardano-ledger; scalus names will differ)
        /*
                hasExactSetOfRedeemers
                validateScriptsWellFormed
                ppViewHashesMatch
                Allegra.validateOutsideValidityIntervalUtxo
                validateOutputTooSmallUTxO
                validateOutputTooBigUTxO
                Shelley.validateWrongNetwork
                Alonzo.validateWrongNetworkInTxBody
                expectScriptsToPass
                evalPlutusScripts
                validateInputSetEmptyUTxO
                */
        // Rules specific to Hydrozoa
        // (Note, Peter 2025-07-02: I'm not sure if these are implemented yet; names may differ)
        /*
                NoStakingValidator // Ensures staking-related fields don't appear in the TxBody
                NoBootstrapAddressesValidator
                NoShelleyAddressBelowV3Validator
                NoDatumHashesValidator
                NoMintingValidator
                L2BalanceValidator // Ensures that the transaction balanced given deposits, withdrawals, and transactions
                 */
        yield ()
    }
}



class L2LedgerRulesTest extends munit.ScalaCheckSuite, ArbitraryInstances {

        test("Sanity check") {
            val context = Context()
            val tx = randomValidTransaction

            assert(L2Validator.validate(context, State(), tx).isRight,
                "random valid transaction should be L2 valid (for now)")
        }


    }
