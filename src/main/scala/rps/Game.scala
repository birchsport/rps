package rps

import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.util.Random

/**
 * Created by jbirchfield on 4/17/15.
 * Implements the algorithm discussed here:
 * http://arstechnica.com/science/2014/05/win-at-rock-paper-scissors-by-knowing-thy-opponent/
 */
object Game extends App {

  /** ******************************************************************
    * ** Initialization
    * *******************************************************************/

  val ROCK = new Rock
  val PAPER = new Paper
  val SCISSORS = new Scissors

  val WIN = 1
  val LOSE = -1
  val DRAW = 0

  //used to pull random pieces when necessary
  val pieces = List(ROCK, PAPER, SCISSORS)

  //used to pull random statuses when necessary
  val statuses = List(WIN, LOSE, DRAW)

  //used to keep track of the moves for the game
  val moves = new ListBuffer[Move]

  //keep track of the players score
  var playerScore = 0

  //keep track of the computer score
  var computerScore = 0

  //this populates the initial state with two random moves and a random state
  moves += Move(randomPiece, randomPiece, randomStatus)

  //let's go!
  play()

  /** ******************************************************************
    * ** Control functions
    * *******************************************************************/

  /**
   * Main play loop.  Fetch the user inout and computer a winner
   */
  def play(): Unit = {
    printMenu()
    for (line <- Source.stdin.getLines) {
      line match {
        case "q" => {
          println(s"Thank you for playing! Final score - player = $playerScore, computer = $computerScore")
          System.exit(0)
        }
        case choice: String => {

          //fetch a piece for our player
          val playerPiece = getPlayerPiece(choice)

          //compute our move
          val computerPiece = computeMove()

          //determine a winner
          val status = playerPiece.compareTo(computerPiece)

          // determine who wins
          // print out the results
          //update game state (scores and moves)
          status match {
            case WIN => {
              println(s"WIN! (p)${playerPiece.name} beats (c)${computerPiece.name}.")
              playerScore += 1
            }
            case LOSE => {
              println(s"LOSE! (c)${computerPiece.name} beats (p)${playerPiece.name}.")
              computerScore += 1
            }
            case DRAW => println(s"DRAW! (c)${computerPiece.name} draws (p)${playerPiece.name}.")
          }
          //add our move to the moves list
          moves += Move(playerPiece, computerPiece, status)
        }
      }

      //print the menu again
      printMenu()
    }
  }

  def randomPiece: Piece = pieces(Random.nextInt(pieces.size))

  def randomStatus: Int = statuses(Random.nextInt(statuses.size))

  /**
   * This is the brains of the game.  We follow the rules outlined in the recent
   * Arstechnica article for following the optimal RPS strategy
   * @return the computed piece
   */
  def computeMove(): Piece = {
    val lastMove = moves.last
    lastMove.status match {
      // we lost, choose whatever was not played
      case LOSE => {
        pieces
          .filter(p => p.name != lastMove.computerPiece.name)
          .filter(p => p.name != lastMove.playerPiece.name)
          .head
      }
      //we won, choose what the player chose last
      case WIN => {
        lastMove.playerPiece
      }
      //we drew, choose a random piece
      case DRAW => {
        randomPiece

      }
    }
  }

  def getPlayerPiece(choice: String): Piece = {
    choice match {
      case "r" => ROCK
      case "p" => PAPER
      case "s" => SCISSORS
      case _ => randomPiece
    }
  }

  def printMenu(): Unit = {
    println(s"Turn ${moves.size} - Score: player = $playerScore, computer = $computerScore")
    println("r = rock")
    println("p = paper")
    println("s = scissors")
    println("q = quit")
    print("Choose: ")
  }

  /** ******************************************************************
    * ** Data Structures
    * *******************************************************************/

  /**
   * base class for al pieces to extend.  It extends Comparable
   * to provide a clean way to calculate the 'win/lose' status when compared
   * to another piece.
   */
  abstract class Piece() extends Comparable[Piece] {

    /**
     * The name of the Piece, by default, the name of the enclosing class
     */
    val name = this.getClass.getSimpleName
  }

  /*
   * Each of the three game pieces Rock, Paper, and Scissors should
   * provide the appropriate logic for
   * determining the /win/loss/ of comparing two pieces together.
   */

  class Rock extends Piece {

    override def compareTo(piece: Piece): Int = {
      piece match {
        case r: Rock => DRAW
        case p: Paper => LOSE
        case s: Scissors => WIN
      }
    }
  }

  class Paper extends Piece {

    override def compareTo(piece: Piece): Int = {
      piece match {
        case r: Rock => WIN
        case p: Paper => DRAW
        case s: Scissors => LOSE
      }
    }
  }

  class Scissors extends Piece {

    override def compareTo(piece: Piece): Int = {
      piece match {
        case r: Rock => LOSE
        case p: Paper => WIN
        case s: Scissors => DRAW
      }
    }
  }

  /**
   * Holds the results of a game
   * @param playerPiece the piece the player chose
   * @param computerPiece the piece the computer chose
   * @param status the game status from the play perspective
   */
  case class Move(playerPiece: Piece, computerPiece: Piece, status: Int)

}
