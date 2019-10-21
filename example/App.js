/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React from 'react';
import {View, Text, Button} from 'react-native';
import RBM from 'react-native-rbm-suggested';

class App extends React.Component {
  state = {
    loading: true,
  };

  componentDidMount() {
    this.initializeRBM();
  }

  initializeRBM = async () => {
    await RBM.init();
    this.setState({loading: false});
  };

  getSuggestedFoodItems = async () => {
    const suggestedFoodItems = await RBM.suggestedFoodItemFor(
      [],
      new Date().getHours(),
      'Europe/Berlin',
    );
    console.log(suggestedFoodItems);
  };

  render() {
    return (
      <View style={{flex: 1, alignItems: 'center', justifyContent: 'center'}}>
        {this.state.loading ? (
          <Text>Loading</Text>
        ) : (
          <Button
            title="getSuggestedFoodItems"
            onPress={this.getSuggestedFoodItems}
          />
        )}
      </View>
    );
  }
}

export default App;
