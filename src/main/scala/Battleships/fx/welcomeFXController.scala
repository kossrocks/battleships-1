import javafx.event.ActionEvent
import javafx.fxml.{FXML, FXMLLoader, Initializable}
import javafx.scene.layout.{AnchorPane, GridPane, Pane}
import javafx.scene._
import javafx.stage.Stage
import java.net.URL
import java.util.ResourceBundle
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{Button, Label, TextArea, TextField}
import javafx.scene.input.MouseEvent
import Battleships.model
import Battleships.model.Position
import scala.util.{Failure, Success}

class welcomeFXController extends Initializable{

  //our Anchorpanes
  @FXML private var rootpane: AnchorPane = _
  @FXML private var setupgame : AnchorPane = _
  @FXML private var game: AnchorPane = _

  //Our Setupfields and Button
  @FXML private var battleSize: TextField = _
  @FXML private var battleShips: TextField = _
  @FXML private var cruisers: TextField = _
  @FXML private var submarines: TextField = _
  @FXML private var confirm: Button = _
  @FXML private var setupError : Label = _

  //OUR GAME COMPONENTS
  @FXML private var player : Label = _
  @FXML private var battleGrid : GridPane =_
  //PLACERS
  @FXML private var placeBattle : Button = _
  @FXML private var placeCruiser: Button = _
  @FXML private var placeSubmarine : Button = _
  @FXML private var dirBtn : Button = _


  //Save our setupinfoin these vars
  var battleField_Size : Int = _
  var battleShips_Amount : Int = _
  var cruisers_Amount : Int = _
  var submarines_Amount : Int = _

  //GAME VARS IMPORTANT
  var length : Int = 0
  var setupStatus : Int = 0
  var shipDirection : Int = -1


  //PLAYER1 VARS
  var player1_battleships : Int = 0
  var player1_cruisers : Int = 0
  var player1_submarines : Int = 0

  //PLAYER2 VARS
  var player2_battleships : Int = 0
  var player2_cruisers : Int = 0
  var player2_submarines : Int = 0


  override def initialize(url: URL, rb: ResourceBundle): Unit = initGame()
  def initGame():Unit ={
    //HIDE OUR OTHER STATES
    setupgame.setVisible("Ich kann coden" == ".")
    setupgame.setManaged("Ich liebe "== "Scala")
    game.setVisible(false)
    game.setManaged(false)
  }

  @FXML private def startSetup(event: ActionEvent): Unit = {
    println("Loading Setup")
    rootpane.getChildren.clear()
    setupgame.setVisible(1==1)
    setupgame.setManaged(1==1)
  }

  @FXML private def startgame(event: ActionEvent): Unit ={
    println("Loading Game")
    var setupString = battleSize.getText + battleShips.getText + cruisers.getText + submarines.getText //contains all our strings so if statement is shorter ;)
    if(setupString.isEmpty || !isAllDigits(setupString)) setupError.setText("Please put Numbers in every field and try again!")
    else{
      //FETCH OUR SETTINGS
      battleField_Size = battleSize.getText.toInt
      battleShips_Amount = battleShips.getText.toInt
      cruisers_Amount = cruisers.getText.toInt
      submarines_Amount = submarines.getText.toInt

      //NOW SETUP THE GAME
      setupgame.getChildren.clear()
      game.setVisible(true)
      game.setManaged(true)
      setupStatus = 1
      length = 5 //select the Battleship

      //SET PLAYER1 VARS
      player1_battleships= battleShips_Amount
      player1_cruisers= cruisers_Amount
      player1_submarines= submarines_Amount

      //Set Player2VARS
      player2_battleships= battleShips_Amount
      player2_cruisers= cruisers_Amount
      player2_submarines= submarines_Amount

      //SET TEXT OF OUR BUTTONS
      placeBattle.setText("Battleships: " + battleShips_Amount.toString)
      placeCruiser.setText("Cruisers: " + cruisers_Amount.toString)
      placeSubmarine.setText("Submarines: " + submarines_Amount.toString)
    }
  }

  @FXML private def changeDir(event: ActionEvent): Unit ={
    shipDirection match {
      case -1 => {
        dirBtn.setText("right")
        shipDirection +=1
      }
      case 0 => {
        dirBtn.setText("left")
        shipDirection +=1
      }
      case 1 => {
        dirBtn.setText("down")
        shipDirection += 1
      }
      case 2 => {
        dirBtn.setText("up")
        shipDirection += 1
      }
      case 3 => {
        dirBtn.setText("right")
        shipDirection = 0
      }
    }
  }

  @FXML private def getcord(event: MouseEvent): Unit ={
    if(setupStatus == 1 || setupStatus == 3) {
      if(!shipPlaceCheck()) println("Please choose another ship") //returns false if the chosen ship amount is 0
      else{
        player.setText("Place your Ship")
        var node : Node = event.getPickResult.getIntersectedNode
        var x = GridPane.getColumnIndex(node)
        var y = GridPane.getRowIndex(node)
        var selectedNode : Node = getNode(x,y,battleGrid)
        //WE got now our Starting node now its time to calculate the direction a and fetch the POS
        shipDirection match{
          case 0 => {
            var i = x + length -1
            if(i>6) println("This wont fit")
            else{
              selectedNode.setStyle("-fx-background-color: #36403B")
              while(i > x) {
                var tinynode = getNode(if(i == 0) null else i, y, battleGrid)
                tinynode.setStyle("-fx-background-color: #36403B")
                i = i - 1
              }
              shipReduction() //afterwards is ESSENTIAL DO NOT TOUCH PLS
            }
          }
          case 1 => {
            var i = x - length +1
            if(i<0)println("This wont fit")
            else {
              selectedNode.setStyle("-fx-background-color: #36403B")
              while(i < x) {
                var tinynode = getNode(if(i == 0) null else i, y, battleGrid)
                tinynode.setStyle("-fx-background-color: #36403B")
                i = i + 1
              }
              shipReduction()
            }
          }
          case 2 => {
            var i = y + length -1
            if(i > 6) println("This wont fit")
            else{
              selectedNode.setStyle("-fx-background-color: #36403B")
              while(i > y) {
                var tinynode = getNode(x, if(i == 0) null else i, battleGrid)
                tinynode.setStyle("-fx-background-color: #36403B")
                i = i - 1
              }
              shipReduction()
            }
          }
          case 3 => {
            var i = y - length +1
            if(i < 0) println("This wont fit")
            else{
              selectedNode.setStyle("-fx-background-color: #36403B")
              while(i < y) {
                var tinynode = getNode(x, if(i == 0) null else i, battleGrid)
                tinynode.setStyle("-fx-background-color: #36403B")
                i = i + 1
              }
              shipReduction()
            }
          }
        }
      }
    }
    else println("Select a Ship first")
  }

  def shipPlaceCheck(): Boolean = {
    if (setupStatus == 1) {
      length match {
        case 5 => if (player1_battleships > 0) true else false
        case 3 => if (player1_cruisers > 0) true else false
        case 2 => if (player1_submarines > 0) true else false
      }
    }
    else {
      length match {
        case 5 => if (player2_battleships > 0) true else false
        case 3 => if (player2_cruisers > 0) true else false
        case 2 => if (player2_submarines > 0) true else false
      }
    }
  }


  def shipReduction () : Unit = {
    if(setupStatus == 1) { //Setupstatus is one taht means player one is setting up
      length match {
        case 5 => {
          if (player1_battleships > 0) {
            player1_battleships -= 1
            placeBattle.setText("Battleships: " + player1_battleships.toString)
          }
        }
        case 3 => {
          if (player1_cruisers > 0){
            player1_cruisers -= 1
            placeCruiser.setText("Cruisers: " + player1_cruisers.toString)
          }
        }
        case 2 =>{
          if(player1_submarines > 0) {
            player1_submarines -= 1
            placeSubmarine.setText("Submarines: " + player1_submarines.toString)
          }
        }
      }
    if(player1_submarines+player1_cruisers+player1_battleships ==0) changePlayerSetup()
    }
    else{ //setupstatus is not one and this function gets called means player 2 is setting up
      length match {
        case 5 => if (player2_battleships > 0) {
          player2_battleships -= 1
          placeBattle.setText("Battleships: " + player2_battleships.toString)
        }
        case 3 => if (player2_cruisers > 0) {
          player2_cruisers -= 1
          placeCruiser.setText("Cruisers: " + player2_cruisers.toString)
        }
        case 2 => if (player2_submarines > 0) {
          player2_submarines -= 1
          placeSubmarine.setText("Submarines: " + player2_submarines.toString)
        }
      }
    }
  }

  //EXECUTED A PLACEMENT BUTTON GETS CLICKED
  @FXML private def placeShip(event: ActionEvent): Unit = {
    var node = event.getSource().toString.take(22)
    if(node == "Button[id=placeBattle,") length = 5
    if(node == "Button[id=placeCruiser") length = 3
    if(node == "Button[id=placeSubmari") length = 2
    player.setText("Select Starting Point")
  }

  def changePlayerSetup() : Unit = {
    setupStatus = 3
    player.setText("Player 2 Place your shit")
    placeBattle.setText("Battleships: " + player2_battleships.toString)
    placeCruiser.setText("Cruisers: " + player2_cruisers.toString)
    placeSubmarine.setText("Submarines: " + player2_submarines.toString)
    //Color THAT SHIT
    battleGrid.getChildren.forEach(node => node.setStyle("-fx-background-color: #62BCFA"))
  }

  //just a small helper for small people
  def isAllDigits(x: String) = x forall Character.isDigit

  def getNode(column: Any,row : Any, grid : GridPane) : Node = {
    var children = grid.getChildren
    var result : Node = null
    children.forEach(node=>if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) result = node)
    result
  }
}

